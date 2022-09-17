import argparse
from copy import copy
from typing import List

from TP3.TP3_python.models.config import Config
from TP3.TP3_python.models.particle import Particle


def parse_arguments() -> Config:
    parser = argparse.ArgumentParser()
    parser.add_argument('-n', '--n_particles', type=int, required=False)
    parser.add_argument('-l', '--board_length', type=float, required=False)
    parser.add_argument('-s', '--static_file', type=str, required=True)
    parser.add_argument('-d', '--dynamic_file', type=str, required=True)
    args = vars(parser.parse_args())

    return Config(args["n_particles"],
                  args["board_length"],
                  args["static_file"],
                  args["dynamic_file"]
                  )


def parse_particles(config: Config):
    particles = _parse_static_file(config)
    return _parse_dynamic_file(config.dynamic_file, particles)


def _parse_static_file(config: Config):
    static_file = open("." + config.static_file)
    n = int(static_file.readline())
    l = float(static_file.readline())
    particles = []

    for i in range(n):
        st_p = list(filter(lambda c: len(c) > 0, static_file.readline().split(" ")))
        particles.append(Particle(i, x=0, y=0, radius=float(st_p[0]), property=float(st_p[1]), vx=0, vy=0))
    static_file.close()

    config.L = l
    config.N = n

    return particles


def parse_input_particles(config: Config) -> List:
    particles = _parse_static_file(config)
    return _parse_dynamic_file(config.dynamic_file, particles)


def _parse_dynamic_file(dynamic_path: str, particles: List[Particle]) -> List[Particle]:
    dynamic_file = open("." + dynamic_path)
    n = len(particles)

    # TODO: refactorear el parseo dinamico para tn
    t0_str = dynamic_file.readline()
    dynamic_file.readline()
    for i in range(n):
        dy_p = list(filter(lambda c: len(c) > 0, dynamic_file.readline().split(" ")))
        particles[i].x = float(dy_p[1])
        particles[i].y = float(dy_p[2])

    return particles


def parse_output_file(config: Config) -> List[List[int]]:
    output_file = open("." + config.output_file)
    neighbours = []
    n = config.N

    for i in range(n):
        n_p = list(filter(lambda c: len(c) > 0, output_file.readline().split(" ")))
        neighbours.append(list(map(lambda id: int(id), n_p)))

    return neighbours


def getEvents(file_path: str):
    dynamic_file = open("." + file_path)

    lines = dynamic_file.readlines()
    events = []
    new_event = []
    for line in lines:
        if line == lines[0]:
            events.append(new_event.copy())
            new_event = []
        elif line == "a\n":
            pass
        else:
            st_p = list(filter(lambda c: len(c) > 0 and c != '\n', line.split(" ")))
            new_event.append(copy(st_p))

    events.pop(0)

    return events


def get_events_small_particles(file_path: str):
    dynamic_file = open("." + file_path)

    lines = dynamic_file.readlines()
    events = []
    new_event = []
    for line in lines:
        if line == lines[0]:
            events.append(new_event.copy())
            new_event = []
        elif line == "a\n" or line[0] == "0":
            pass
        else:
            st_p = list(filter(lambda c: len(c) > 0 and c != '\n', line.split(" ")))
            new_event.append(copy(st_p))

    events.pop(0)

    return events
