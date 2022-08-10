import argparse
import getopt
import sys
from typing import List

from models.config import Config
from models.particle import Particle


def parse_arguments() -> Config:
    parser = argparse.ArgumentParser()
    parser.add_argument('-n', '--n_particles', type=int, required=False)
    parser.add_argument('-m', '--n_cells', type=int, required=True)
    parser.add_argument('-r', '--radius', type=float, required=True)
    parser.add_argument('-l', '--cell_length', type=float, required=False)
    parser.add_argument('-s', '--static_file', type=str, required=True)
    parser.add_argument('-d', '--dynamic_file', type=str, required=True)
    parser.add_argument('-o', '--output_file', type=str, required=True)
    args = vars(parser.parse_args())

    return Config(args["n_particles"],
                  args["n_cells"],
                  args["radius"],
                  args["cell_length"],
                  args["static_file"],
                  args["dynamic_file"],
                  args["output_file"]
                  )


def parse_input_particles(config: Config) -> List:
    particles = _parse_static_file(config)
    return _parse_dynamic_file(config.dynamic_file, particles)


def _parse_static_file(config: Config) -> List[Particle]:
    static_file = open("." + config.static_file)
    n = int(static_file.readline())
    l = int(static_file.readline())
    particles = []

    for i in range(n):
        st_p = list(filter(lambda c: len(c) > 0, static_file.readline().split(" ")))
        particles.append(Particle(i, x=0, y=0, radius=float(st_p[0]), property=float(st_p[1])))
    static_file.close()

    config.L = l
    config.N = n

    return particles


def _parse_dynamic_file(dynamic_path: str, particles: List[Particle]) -> List[Particle]:
    dynamic_file = open("." + dynamic_path)
    n = len(particles)

    # TODO: refactorear el parseo dinamico para tn
    t0_str = dynamic_file.readline()

    for i in range(n):
        dy_p = list(filter(lambda c: len(c) > 0, dynamic_file.readline().split(" ")))
        particles[i].x = float(dy_p[0])
        particles[i].y = float(dy_p[1])

    return particles


def parse_output_file(config: Config) -> List[List[int]]:
    output_file = open("." + config.output_file)
    neighbours = []
    n = config.N

    for i in range(n):
        n_p = list(filter(lambda c: len(c) > 0, output_file.readline().split(" ")))
        neighbours.append(list(map(lambda id: int(id), n_p)))

    return neighbours
