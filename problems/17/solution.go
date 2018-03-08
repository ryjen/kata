package main

import (
	"sort"
	"strings"
)

type PathPart struct {
	Value string
	Depth int
}

func (p PathPart) IsFile() bool {
	return strings.Contains(p.Value, ".")
}

func (p PathPart) Len() int {
	return len(p.Value)
}

type FilePath []PathPart

func (f FilePath) PathLen() int {
	size := 0
	for _, part := range f {
		size += part.Len()
		size += 1
	}
	return size
}

func (f FilePath) Len() int { return len(f) }
func (f FilePath) Less(i, j int) bool {
	if f[i].Depth != f[j].Depth {
		return f[i].Depth < f[j].Depth
	}

	return strings.Compare(f[i].Value, f[j].Value) < 0
}
func (f FilePath) Swap(i, j int) { f[i], f[j] = f[j], f[i] }

func (f FilePath) Last() PathPart {
	return f[len(f)-1]
}

func getDepth(input string) int {

	count := 1

	for _, c := range input {
		if c != '\t' {
			break
		}
		count++
	}
	return count
}

func parsePathPart(input string) FilePath {
	pieces := strings.Split(input, "\n")

	var path FilePath

	for _, piece := range pieces {
		depth := getDepth(piece)
		trimmed := strings.TrimSpace(piece)
		part := PathPart{
			Value: trimmed,
			Depth: depth,
		}
		path = append(path, part)
	}
	sort.Sort(path)
	return path
}

/**
 * Solution:
 *
 * Parse path strings into a tree based on heirarchy and length.
 * Follow each path, recording the length and return the largest.
 */
func SolutionList(input string) int {

	parts := parsePathPart(input)

	var path FilePath

	max := 0

	for _, part := range parts {

		if part.IsFile() {
			current := path.PathLen() + part.Len()

			if current > max {
				max = current
			}
		} else {
			if path.Len() > 0 {
				lastPath := path.Last()

				for lastPath.Depth >= part.Depth {
					path = path[:len(path)-1]
					lastPath = path.Last()
				}
			}
			path = append(path, part)
		}
	}

	return max
}
