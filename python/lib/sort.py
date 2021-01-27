
def merge(A: list) -> list:

    def __merge(A: list, B: list) -> list:
        C = []
        while len(A) > 0 and len(B) > 0:
            tmp = A.pop() if A[0] < B[0] else B.pop()
            C.append(tmp)

        for i in A:
            C.append(i)

        for i in B:
            C.append(i)

        return C


    N = len(A)

    if N == 1:
        return A

    middle = int(N/2)

    left = A[:middle]
    right = A[middle:]

    l = merge(left)
    r = merge(right)

    return __merge(l, r)


