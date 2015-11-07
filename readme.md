Project developed for GIS classes by Cezary Pawlowski and Pawel Banasiak.

To preview .gv files use Graphviz (graph visualization software).

dot file.gv -Tsvg -o image.svg

dot file.gv -Tpdf -o image.pdf

# generator #

Program developed for directed graph generation.

Invocations:

./run.sh -logLevel=off -probMode -v=10 -outputFile=p10.gv -p=0.25
./run.sh -logLevel=off -strongMode -v=100 -outputFile=s100.gv -e=2000 -c=7

# tarjan #

Tarjan's strongly connected components algorithm.

Invocations:

./run.sh -logLevel=off -inputFile=../data/s100.gv -time
