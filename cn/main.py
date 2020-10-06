import sparse
import time
import numpy as np

import matplotlib.pyplot as plt
import seaborn as sns

sns.set()


class sparse_matrix:
    def __init__(self, A, b):
        self.data = [{index: val for index, val in enumerate(line) if val != 0} for line in A]
        self.size = self.data.__len__()
        self.b = b
        self.epsilon = 0.0000001

    def mull_line(self, line, val_to_mull):
        return {key: val_to_mull * val for key, val in self.data[line].items() if
                not -self.epsilon < val_to_mull * val < self.epsilon}

    def div(self, line: dict, line_to_div: int):
        for key, value in line.items():
            if key in self.data[line_to_div].keys():
                self.data[line_to_div][key] -= line[key]
            else:
                self.data[line_to_div][key] = line[key]
            if -self.epsilon < self.data[line_to_div][key] < self.epsilon:
                self.data[line_to_div][key] = 0

    def solve(self):
        for collumn in range(self.size):
            # start_time = time.time()
            for line in range(collumn + 1, self.size):
                if collumn in self.data[line].keys():
                    m = self.data[collumn][line] / self.data[collumn][collumn]
                    self.b[line] -= self.b[collumn] * m
                    line_with_m = self.mull_line(collumn, m)
                    self.div(line_with_m, line)

    def get_sollution(self):
        sol = {}
        for line in reversed(range(self.size)):
            inner_sum = 0
            for collumn in range(0, self.size - line - 1):
                index = self.size - collumn - 1
                if index in self.data[line].keys():
                    inner_sum += sol[index] * self.data[line][index]
            sol[line] = (self.b[line] - inner_sum) / self.data[line][line]
        return sol


def test(size=100, sparsity=0.0008):
    time_without_alg = []
    time_with_alg = []
    nnzs = []
    for i in range(10):
        print(i)
        A, b, nnz = sparse.generate_random_sparse_symmetric(size, sparsity)
        nnzs.append(nnz)
        start_time = time.time()
        A1, b1 = sparse.transform_to_band(A, b)
        solver_without = sparse_matrix(A1, b1)
        solver_without.solve()
        time_without_alg.append(time.time() - start_time)
        #
        start_time = time.time()
        A1, b1 = sparse.transform_to_band(A, b, True)
        solver_with = sparse_matrix(A1, b1)
        solver_with.solve()
        time_with_alg.append(time.time() - start_time)
    plt.plot(time_without_alg, '*-', label='without')
    plt.plot(time_with_alg, '*-', label='with')
    plt.legend()
    plt.show()
    print("avg without:", '{0:.10f}'.format(sum(time_without_alg) / len(time_without_alg)))
    print("avg with:", '{0:.10f}'.format(sum(time_with_alg) / len(time_with_alg)))


test(sparsity=0.05)

