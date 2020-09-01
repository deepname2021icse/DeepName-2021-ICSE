# A Context-based Automated Approach for Method Name Consistency Checking and Suggestion

Misleading method names in software projects can confuse developers, which may lead to software defects and affect code understandability. In this paper, we present DEEPNAME, a context-based, deep learning approach to detect method name inconsistencies and suggest a proper name for a method. The key departure point is the philosophy of “Show Me Your Friends, I’ll Tell You Who You Are”. Unlike the state-of-the-art approaches, in addition to the method’s body, we also consider the interactions of the current method under study with the other ones including the caller and callee methods, and the sibling methods in the same enclosing class. The sequences of sub-tokens in the program entities’ names in the contexts are extracted and used as the input for an RNN-based encoder-decoder to produce the representations for the current method. We modify that RNN model to integrate the copy mechanism and our newly developed component, called non-copy mechanism, to emphasize on the possibility of a certain sub-token not copied to follow the current sub-token in the currently generated method name.

We conducted several experiments to evaluate DEEPNAME on large datasets with +14M methods. For consistency checking, DEEPNAME improves the state-of-the-art approach by 2.1%, 19.6%, and 11.9% relatively in recall, precision, and F-score, respectively. For name suggestion, DEEPNAME improves relatively over the state-of-the-art approaches in precision (1.8%–30.5%), recall (8.8%–46.1%), and F-score (5.2%–38.2%). To assess DEEPNAME’s usefulness, we detected inconsistent methods and suggested new names in active projects. Among 50 pull requests, 12 were merged into the main branch. In total, in 30/50 cases, the team members agree that our suggested names are more meaningful than the current names.

****
# This repository is under clearing and reformatting

# Dataset used in the paper:

Liu et al. : https://github.com/SerVal-DTF/debug-method-name

MNire: https://sonvnguyen.github.io/mnire/