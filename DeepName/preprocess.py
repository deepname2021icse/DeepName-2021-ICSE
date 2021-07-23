import os
import subprocess
import re
import json
import gensim
import numpy as np


def method_selection(string_list):
    level = 0
    methods = []
    method_check = False
    finish_level = 0
    for i in range(len(string_list)):
        if string_list[i].find(":{") != -1:
            level = level + 1
            if not method_check:
                if string_list[i].find("Method") != -1:
                    method = [string_list[i].strip()]
                    method_check = True
            else:
                if string_list[i].find("MethodDeclaration;body") != -1:
                    finish_level = level - 1
                method.append(string_list[i].strip())
        else:
            if string_list[i].find("}") != -1:
                level = level - 1
                if method_check:
                    method.append(string_list[i].strip())
                    if level == finish_level:
                        finish_level = 0
                        methods.append(method)
                        method_check = False
            else:
                if method_check:
                    method.append(string_list[i].strip())
    return methods


def get_method_name(method):
    start = 0
    for line in method:
        if line.find("MethodDeclaration;name:") != -1:
            start = 1
        if start == 1:
            if line.find("SimpleName;identifier;") != -1:
                return line.split(";")[2]
            if line.find("}") != -1:
                start = 0


def get_method_lines(file_name):
    input_file = open(file_name)
    lines = input_file.readlines()
    methods_get = method_selection(lines)
    line_num = {}
    for method in methods_get:
        method_name = get_method_name(method)
        method_lines = []
        for line in method:
            if len(line.split(";")) > 3:
                line_n = int(line.strip().split(";")[-1])
                method_lines.append(line_n)
        line_num[method_name] = [min(method_lines), max(method_lines)]
    return line_num


def get_method_string(file_name, line_num):
    input_file = open(file_name)
    lines = input_file.readlines()
    method_string = {}
    for method_name in line_num.keys():
        start_line = line_num[method_name][0]
        end_line = line_num[method_name][1]
        line_counter = start_line
        method_str = ""
        while line_counter <= end_line:
            method_str = method_str + " " + lines[line_counter - 1].strip()
            line_counter = line_counter + 1
        method_str_blind = method_str.replace(" " + method_name + "(", " (", 1)
        method_string[method_name] = [string_cleaner(method_str), string_cleaner(method_str_blind)]
    return method_string


def get_enclosing_lines(file_name):
    input_file = open(file_name)
    lines = input_file.readlines()
    class_lines = []
    for line in lines:
        if len(line.split(";")) > 3:
            line_n = int(line.strip().split(";")[-1])
            class_lines.append(line_n)
    return [min(class_lines), max(class_lines)]


def get_enclosing_string(file_name, line_num, method_lines):
    input_file = open(file_name)
    lines = input_file.readlines()
    start_line = line_num[0]
    end_line = line_num[1]
    line_counter = start_line
    class_str = ""
    while line_counter <= end_line:
        check = 1
        for method in method_lines.keys():
            if int(method_lines[method][0]) <= line_counter <= int(method_lines[method][1]):
                check = 0
                break
        if check == 1:
            class_str = class_str + " " + lines[line_counter - 1].strip()
        line_counter = line_counter + 1
    return string_cleaner(class_str)


def check_upper_case(string_input):
    return_list = []
    for i in range(len(string_input)):
        if string_input[i].isupper() and i != 0:
            first = string_input[:i]
            second = string_input[i:]
            return_list.append(first)
            return_list.extend(check_upper_case(second))
            break
    if len(return_list) < 1:
        return_list.append(string_input)
    return return_list


def string_cleaner(input_string):
    new_string = re.sub(r"[^a-zA-Z0-9]", " ", input_string.strip())
    tokens = new_string.split(" ")
    marker = []
    for i in range(len(tokens)):
        if tokens[i] in ["public", "private", "class", "static"]:
            marker.append(i)
    for delete in marker:
        tokens.pop(delete)
    for i in range(len(tokens)):
        break_down_list = check_upper_case(tokens[i])
        if len(break_down_list) > 1:
            tokens[i] = break_down_list[0]
            for j in range(len(break_down_list)):
                if j != 0:
                    tokens.insert(i + j + 1, break_down_list[j])
    while '' in tokens:
        tokens.remove('')
    output_string = " ".join(tokens)
    return output_string


def get_caller_callee_string(method_root, method_name, call_relation, method_string_list, method_path_list):
    caller = []
    callee = []
    caller_str = ""
    callee_str = ""
    method_name_str = method_root + "." + method_name
    for call in call_relation:
        call_set = call.strip().split(" ")
        if call_set[0].find(method_name_str) != -1:
            callee.append(call_set[1])
        else:
            if call_set[1].find(method_name_str) != -1:
                caller.append(call_set[0])
    for caller_ in caller:
        checker = 1
        for i in range(len(method_string_list)):
            if checker == 1:
                for method in method_string_list[i].keys():
                    if caller_.find(method_path_list[i] + "." + method) != -1:
                        caller_str = caller_str + " " + method_string_list[i][method][0]
                        checker = 0
                        break
            else:
                break
    for callee_ in callee:
        checker = 1
        for i in range(len(method_string_list)):
            if checker == 1:
                for method in method_string_list[i].keys():
                    if callee_.find(method_path_list[i] + "." + method) != -1:
                        callee_str = callee_str + " " + method_string_list[i][method][0]
                        checker = 0
                        break
            else:
                break
    return caller_str + " " + callee_str


def get_sibling_string(siblings, method_name):
    output = ""
    for method in siblings.keys():
        if method != method_name:
            output = output + " " + siblings[method][0]
    return output


def learn_embedding(string_input):
    # This function can be modified for different embedding techniques. Also can be rewrite to load existing embedding
    w2v = gensim.models.Word2Vec(sentences=string_input, vector_size=128, window=5, min_count=1, workers=4)
    if os.path.exists('embedding.model'):
        os.remove('embedding.model')
    w2v.save('embedding.model')
    return w2v


def data_loader(root, data_type):
    data_set = []
    embedding_string = ""
    data_set_max_length = [0, 0, 0, 0, 0]
    folders = os.listdir(root)
    # Each project inside is a sub folder
    for folder in folders:
        dir_ = os.path.join(root, folder)
        if os.path.isdir(dir_):
            method_sets = []
            class_sets = []
            path_sets = []
            for roots, dirs, files in os.walk(dir_):
                for file in files:
                    file_path = os.path.join(roots, file)
                    if file_path.endswith('.java'):
                        file_output = {}
                        subprocess.call("java -jar " + os.getcwd() + "ASTgenerate.jar " + file_path + " " + os.path.join(os.getcwd(), "temp.txt"), shell=True)
                        ast_file = os.path.join(os.getcwd(), "temp.txt")
                        method_lines_list = get_method_lines(ast_file)
                        class_line_list = get_enclosing_lines(ast_file)
                        method_string_list = get_method_string(file_path, method_lines_list)
                        for method in method_string_list.keys():
                            embedding_string = embedding_string + " " + method_string_list[method][0]
                        class_str = get_enclosing_string(file_path, class_line_list, method_lines_list)
                        embedding_string = embedding_string + " " + class_str
                        file_output['methods'] = method_string_list
                        method_sets.append(method_string_list)
                        file_output['class'] = class_str
                        class_sets.append(class_str)
                        # this postion need to be changed based on the operation system and project you are testing
                        class_path = file_path.split("\\")[-3:]
                        class_path[-1] = class_path[-1].split(".")[0]
                        file_output['path'] = ".".join(class_path)
                        path_sets.append(file_output['path'])
                        output_file = open("temp/" + file_output['path'] + ".json", "w")
                        json.dump(file_output, output_file)
                        os.remove(ast_file)
            # This can be changed based on each project where you put call graph
            call_graph_file = open(dir_ + "\\call_graph.txt")
            call_edges = call_graph_file.readlines()
            if data_type == 2:
                for i in range(len(method_sets)):
                    for method in method_sets[i].keys():
                        fea_1 = method_sets[i][method][1].split()
                        fea_2 = get_caller_callee_string(path_sets[i], method, call_edges, method_sets, path_sets).split()
                        fea_3 = get_sibling_string(method_sets[i], method).split()
                        fea_4 = class_sets[i].split()
                        fea_5 = string_cleaner(method).split()
                        len_1 = len(fea_1)
                        len_2 = len(fea_2)
                        len_3 = len(fea_3)
                        len_4 = len(fea_4)
                        len_5 = len(fea_5)
                        if data_set_max_length[0] < len_1:
                            data_set_max_length[0] = len_1
                        if data_set_max_length[1] < len_2:
                            data_set_max_length[1] = len_2
                        if data_set_max_length[2] < len_3:
                            data_set_max_length[2] = len_3
                        if data_set_max_length[3] < len_4:
                            data_set_max_length[3] = len_4
                        if data_set_max_length[4] < len_5:
                            data_set_max_length[4] = len_5
                        data_set.append([fea_1, fea_2, fea_3, fea_4, fea_5])
            if data_type == 1:
                # This can be changed based on where you put the file to point to the method that needs to be checked
                check_pointer = open(dir_ + "\\checking.txt")
                lines = check_pointer.readlines()
                for line in lines:
                    method_path = line.strip().split(".")[-4:-1]
                    method_name = line.strip().split(".")[-1]
                    if method_name.endswith(".java"):
                        method_name = method_name.replace(".java", "")
                    for i in range(len(method_sets)):
                        for method in method_sets[i].keys():
                            if path_sets[i] == ".".join(method_path) and method == method_name:
                                fea_1 = method_sets[i][method][1].split()
                                fea_2 = get_caller_callee_string(path_sets[i], method, call_edges, method_sets, path_sets).split()
                                fea_3 = get_sibling_string(method_sets[i], method).split()
                                fea_4 = class_sets[i].split()
                                fea_5 = string_cleaner(method).split()
                                len_1 = len(fea_1)
                                len_2 = len(fea_2)
                                len_3 = len(fea_3)
                                len_4 = len(fea_4)
                                len_5 = len(fea_5)
                                if data_set_max_length[0] < len_1:
                                    data_set_max_length[0] = len_1
                                if data_set_max_length[1] < len_2:
                                    data_set_max_length[1] = len_2
                                if data_set_max_length[2] < len_3:
                                    data_set_max_length[2] = len_3
                                if data_set_max_length[3] < len_4:
                                    data_set_max_length[3] = len_4
                                if data_set_max_length[4] < len_5:
                                    data_set_max_length[4] = len_5
                                data_set.append([fea_1, fea_2, fea_3, fea_4, fea_5])
    embedding_string = embedding_string.split()
    embedding_model = learn_embedding([embedding_string])
    for i in range(len(data_set)):
        for j in range(len(data_set[i])):
            for k in range(len(data_set[i][j])):
                data_set[i][j][k] = embedding_model.wv[data_set[i][j][k]]
    return data_set, data_set_max_length


