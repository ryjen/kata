package main

type line struct {
	length int
	value  []string
}

func (in *line) next() *line {

	out := &line{
		value:  nil,
		length: 0,
	}

	for _, word := range in.value {
		l := len(word)

		if out.length+len(out.value)+1 >= in.length {
			break
		}
		out.length += l
		out.value = append(out.value, word)
	}

	pos := len(out.value)
	if pos > 0 {

		slots := pos - 1
		diff := in.length - out.length

		mod := diff % slots

		pad := diff / slots

		for i := 0; i < slots; i++ {

			for j := 0; j < pad+mod; j++ {
				out.value[i] += " "
			}
			if mod > 0 {
				mod -= 1
			}
		}

		in.value = in.value[pos:]
	}
	return out
}

func Solution(k int, words []string) []string {
	in := &line{length: k, value: words}

	out := in.next()

	var result []string

	for out.length > 0 {
		var buf string

		for _, word := range out.value {
			buf += word
		}

		result = append(result, buf)

		out = in.next()
	}

	return result
}
