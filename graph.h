#ifndef SCRAWBLE_GRAPH_H
#define SCRAWBLE_GRAPH_H

#include <map>
#include <set>
#include <string>

namespace graphs
{
    namespace flags
    {
        typedef enum {
            none = 0,
            directed = (1 << 0),
        } type;
    }

    namespace degree
    {
        typedef enum { both, out, in } type;
    }

    class edge
    {
       public:
        bool operator<(const edge &other) const
        {
            return false;
        }
    };

    class weighted_edge : public edge
    {
       public:
        weighted_edge(int value) : value_(value)
        {
        }
        int weight() const
        {
            return value_;
        }

        bool operator<(const weighted_edge &other) const
        {
            return value_ < other.value_;
        }

       private:
        int value_;
    };
}

template <typename V, typename E = graphs::edge>
class graph
{
    struct __link {
        V to;
        E edge;
        __link(V a, E b) : to(a), edge(b)
        {
        }

        bool operator==(const V other) const
        {
            return to == other;
        }

        bool operator<(const struct __link &other) const
        {
            if (to < other.to) {
                return true;
            }
            return edge < other.edge;
        }
    };

   public:
    const static int NO_SUCH_VERTEX = -1;

    typedef E edge_type;

    typedef struct __link link_type;

    typedef std::set<link_type> list_type;

   private:
    typedef std::map<V, list_type> container_type;

   public:
    graph(const graphs::flags::type &flags = graphs::flags::none)
        : flags_(flags)
    {
    }

    graph &push(const V &vertex)
    {
        if (values_.count(vertex) == 0) {
            values_[vertex] = graph::list_type();
        }
        return *this;
    }

    int remove(const V &vertex)
    {
        int erased = values_.erase(vertex);

        if (erased > 0) {
            for (auto &it : values_) {
                it.second.erase(vertex);
            }
        }
        return erased;
    }

    int size() const
    {
        return values_.size();
    }

    graph &push(const V &a, const V &b, const E &edge)
    {
        values_[a].emplace(b, edge);
        if (!is_set(graphs::flags::directed)) {
            values_[b].emplace(a, edge);
        }
        return *this;
    }

    graph &push(const V &a, const V &b)
    {
        return push(a, b, edge_type());
    }

    bool is_edge(const V &a, const V &b) const
    {
        auto it = values_.find(a);
        if (it != values_.end()) {
            for (auto &link : it->second) {
                if (link.to == b) {
                    return true;
                }
            }
        }
        return false;
    }

    edge_type edge(const V &a, const V &b) const
    {
        auto it = values_.find(a);
        if (it != values_.end()) {
            for (auto &link : it->second) {
                if (link.to == b) {
                    return link.edge;
                }
            }
        }

        throw NO_SUCH_VERTEX;
    }

    const list_type adjacent(const V &v) const
    {
        auto list = values_.find(v);

        if (list == values_.end()) {
            return list_type();
        }
        return list->second;
    }

    int degree(const V &v,
               const graphs::degree::type &type = graphs::degree::both) const
    {
        switch (type) {
            case graphs::degree::out: {
                auto list = values_.find(v);

                if (list == values_.end()) {
                    return 0;
                }

                return list->second.size();
            }
            case graphs::degree::in: {
                int count = 0;
                for (auto &it : values_) {
                    for (auto &link : it.second) {
                        if (link.to == v) {
                            count++;
                        }
                    }
                }
                return count;
            }
            case graphs::degree::both:
                return degree(v, graphs::degree::out) +
                       degree(v, graphs::degree::in);
        }
    }

    graph &clear()
    {
        values_.clear();
        return *this;
    }

    std::string to_string() const
    {
        std::string buf;

        for (auto it : values_) {
            buf += it.first;
            buf += " → ";
            auto adj = it.second;
            for (auto it = adj.begin(); it != adj.end(); ++it) {
                buf += it->to;
                buf += ",";
            }

            if (adj.size() > 0) {
                buf.erase(buf.length() - 1);
            }

            buf += "\n";
        }
        return buf;
    }

   private:
    bool is_set(const graphs::flags::type &flag)
    {
        return (flags_ & flag);
    }

    graphs::flags::type flags_;

    container_type values_;
};

template <typename V, typename E = graphs::edge>
std::ostream &operator<<(std::ostream &out, const graph<V, E> &graph)
{
    out << graph.to_string();
    return out;
}

#endif
