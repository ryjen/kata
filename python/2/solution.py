
def solution(A, K):
 
    if len(A) == 0:
        return A

    for i in range(K):
        tmp = A.pop()
        A = [tmp, *A]

    return A
