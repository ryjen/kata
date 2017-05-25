#include <bandit/bandit.h>
#include <scrawble/graph.h>

using namespace bandit;

go_bandit([]() {

    describe("a graph", []() {
        it("can add an edge", []() {
            graph<int> graph;
            graph.push(0);
            graph.push(1);
            graph.push(2);
            graph.push(3);
            graph.push(1, 2);

            AssertThat(graph.is_edge(1, 2), IsTrue());
            AssertThat(graph.is_edge(2, 1), IsTrue());
        });

        describe("a vertex", []() {
            it("has adjacent edges", []() {
                graph<int, graphs::weighted_edge> g;

                g.push(0, 1, graphs::weighted_edge(3));
                g.push(1, 3, graphs::weighted_edge(4));
                g.push(2, 1, graphs::weighted_edge(1));
                g.push(3, 4, graphs::weighted_edge(3));

                int expected[] = {1, 3, 4};

                auto adj = g.adjacent(1);

                int count = 0;

                for (auto &link : adj) {
                    AssertThat(link.edge.weight(), Equals(expected[count++]));
                }
            });
        });
    });

    describe("a directed graph", []() {
        it("has a degree", []() {
            graph<int> graph(graphs::flags::directed);

            graph.push(1, 2);

            AssertThat(graph.degree(1, graphs::degree::both), Equals(1));

            AssertThat(graph.degree(1, graphs::degree::in), Equals(0));

            AssertThat(graph.degree(1, graphs::degree::out), Equals(1));

            graph.push(3, 1);

            AssertThat(graph.degree(1, graphs::degree::both), Equals(2));

            AssertThat(graph.degree(1, graphs::degree::in), Equals(1));

            AssertThat(graph.degree(1, graphs::degree::out), Equals(1));

            graph.push(1, 0);

            AssertThat(graph.degree(1, graphs::degree::both), Equals(3));

            AssertThat(graph.degree(1, graphs::degree::in), Equals(1));

            AssertThat(graph.degree(1, graphs::degree::out), Equals(2));

            // no connection to vertex 1
            graph.push(2, 0);

            AssertThat(graph.degree(1, graphs::degree::both), Equals(3));

            graph.push(2, 1);

            AssertThat(graph.degree(1, graphs::degree::both), Equals(4));

            AssertThat(graph.degree(1, graphs::degree::in), Equals(2));

            AssertThat(graph.degree(1, graphs::degree::out), Equals(2));
        });
    });

    describe("an undirected graph", []() {
        it("has a degree", []() {
            graph<int> graph;

            graph.push(1, 2);

            AssertThat(graph.degree(1), Equals(2));

            graph.push(3, 1);

            AssertThat(graph.degree(1), Equals(4));

            graph.push(1, 0);

            AssertThat(graph.degree(1), Equals(6));

            // no connection to vertex 1
            graph.push(2, 0);

            AssertThat(graph.degree(1), Equals(6));

            // already added edge
            graph.push(2, 1);

            AssertThat(graph.degree(1), Equals(6));
        });
    });

});