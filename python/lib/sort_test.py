from sort import merge

def test_merge():
    value = [23, 54, 12, 2, 56, -9]
    expected = [-9, 2, 12, 23, 54, 56]

    actual = merge(value)

    for i, val in enumerate(actual):
        assert val == expected[i]

