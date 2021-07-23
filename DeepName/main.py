import preprocess
import numpy as np
import model


def transform_padding(input_list, max_length, embedding_size):
    result = np.zeros([len(input_list), max_length, embedding_size])
    for i, row in enumerate(input_list):
        for j, val in enumerate(row):
            result[i][j] = val
    return result


def prepare_data(projects_root, research_type):
    # for more detailed information about input data requirements, please check function data_loader.
    data_set, data_length = preprocess.data_loader(projects_root, research_type)
    feature_1 = []
    feature_2 = []
    feature_3 = []
    feature_4 = []
    target = []
    for data in data_set:
        feature_1.append(data[0])
        feature_2.append(data[1])
        feature_3.append(data[2])
        feature_4.append(data[3])
        target.append(data[4])
    feature_1 = transform_padding(feature_1, data_length[0], 128)
    feature_2 = transform_padding(feature_2, data_length[0], 128)
    feature_3 = transform_padding(feature_3, data_length[0], 128)
    feature_4 = transform_padding(feature_4, data_length[0], 128)
    target = transform_padding(target, data_length[0], 128)
    return [feature_1, feature_2, feature_3, feature_4, target]


#Here the experiment_type = 2 means the method name generation. If you want to test method name consistency, please change it to 1.
experiment_type = 2
prepare_data("Please put the root for the projects folder", experiment_type)
model.training(experiment_type)
model.testing(experiment_type)