import networkx as nx
import os


# These code used for combining the Doxygen output and generate the call graph input
# All Doxygen generated call graphs need to be put in ./graphs. Only accept .dot file.
path = os.getcwd()
G = nx.DiGraph()
node_list = {}
id_counter = 0
for root, dir, files in os.walk(path + "/graphs"):
    for file in files:
        if file.endswith(".dot"):
            file_path = os.path.join(root, file)
            new_graph = nx.Graph(nx.drawing.nx_pydot.read_dot(file_path))
            temp_dic = {}
            for node_id, node_content in list(new_graph.nodes(data=True)):
                node_value = node_content['label'].replace("\l", "")
                temp_dic[node_id] = node_value
                if node_value not in node_list.keys():
                    id_counter = id_counter + 1
                    node_list[node_value] = id_counter
                    G.add_node(id_counter, name=node_value)
            for start_node, end_node, _ in list(new_graph.edges(data=True)):
                G.add_edge(node_list[temp_dic[start_node]], node_list[temp_dic[end_node]])
output_file = open("call_graph.txt","a")
for start_node, end_node, _ in list(G.edges(data=True)):
    if G.nodes[start_node]['name'].find(".") != -1 and G.nodes[end_node]['name'].find(".") != -1:
        print(G.nodes[start_node]['name'].replace("\"", ""), " ", G.nodes[end_node]['name'].replace("\"", ""), file=output_file)
output_file.close()
