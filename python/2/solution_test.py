from solution import solution
import random

def makeArray(size):
    return [random.randint(-1000,1000) for x in range(size)]

inputs = [
    ([3, 8, 9, 7, 6], 3),
    ([0, 0, 0], 1),
    ([1, 2, 3, 4], 4),
    ([-100, -50, 123, 0, 393, 123, 32, -1], 50),
    (makeArray(100), 100),
    ([], 3)
]

outputs = [
    [9, 7, 6, 3, 8],
    [0, 0, 0],
    [1, 2, 3, 4]
]

def validate(result, expected):
    for i, val in enumerate(result):
        assert val == expected[i]


def test_solution():
    for i, val in enumerate(inputs):
        result = solution(*val)
        
        if i < len(outputs):
            validate(result, outputs[i])
        else:
            print("Returned {}".format(result))


