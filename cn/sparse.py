from scipy.sparse import random
import numpy as np
from scipy import stats
import matplotlib.pylab as plt
from scipy.sparse import csr_matrix, eye
from scipy.sparse.csgraph import reverse_cuthill_mckee
from itertools import chain


class CustomRandomState(np.random.RandomState):
    def randint(self, k):
        i = np.random.randint(k)
        return i - i % 2


def are_equal(a, b):
    return set(list(chain.from_iterable(a))).difference(set(list(chain.from_iterable(b)))).__len__() == 0


def generate_random_sparse_symmetric(size=5, prop=0.0006):
    np.random.seed(12345)
    rs = CustomRandomState()
    rvs = stats.poisson(250, loc=10).rvs
    S = random(size, size, density=prop, random_state=rs, data_rvs=rvs, dtype="int32")
    B = random(size, 1, density=1, random_state=rs, data_rvs=rvs, dtype="int32")
    A = S + S.transpose() + eye(size)
    return A, B, csr_matrix(A).nnz


def transform_to_band(A, B, alg=False):
    if not alg:
        return A.A.tolist(), list(chain.from_iterable(B.A.tolist()))
    # res = A @ B
    # print("A=", A.A)
    # print("b=", B.A)
    #
    # print()

    a = csr_matrix(A)
    p = reverse_cuthill_mckee(a, symmetric_mode=True)
    band_a = A[np.ix_(p, p)]
    band_b = csr_matrix(B.A[p, :])
    # print("p=", p)

    # band_result = band_a @ band_b
    # print("band_a=", band_a.toarray())
    # print("band_b=", band_b.toarray())

    # print(are_equal(band_result.toarray(), res.A))
    # print("a*b=", band_result.toarray())

    # print("A*b=", res.A)
    # print("nz", band_a.nnz)
    # plt.spy(A.A, markersize=marksize)
    # plt.show()
    # plt.spy(band_a.toarray(), markersize=marksize)
    # plt.show()
    return band_a.toarray().tolist(), list(chain.from_iterable(band_b.toarray().tolist()))
