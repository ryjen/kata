package main

func knuthTable(W string) []int {
	T := make([]int, len(W))
	pos := 1
	cnd := 0

	T[0] = -1

	for pos < len(W) {
		if W[pos] == W[cnd] {
			T[pos] = T[cnd]
			pos++
			cnd++
		} else {
			T[pos] = cnd
			cnd = T[cnd]

			for cnd >= 0 && W[pos] != W[cnd] {
				cnd = T[cnd]
			}

			pos++
			cnd++
		}
	}

	if pos < len(T) {
		T[pos] = cnd
	}

	return T
}

func knuthSearch(S string, W string) []int {

	m, i := 0, 0
	T := knuthTable(W)
	var P []int

	for m+i < len(S) {
		if W[i] == S[m+i] {
			if i+1 == len(W) {
				P = append(P, m)
				m = m + i - T[i]
				i = T[i]
			} else {
				i++
			}
		} else {
			if T[i] > -1 {
				m = m + i - T[i]
				i = T[i]
			} else {
				m = m + i + 1
				i = 0
			}
		}
	}
	return P
}

func Solution(D []string, W string) []string {

	var R []string

	for _, d := range D {

		p := knuthSearch(W, d)

		if len(p) > 0 {
			R = append(R, d)
		}
	}

	return R
}
