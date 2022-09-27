import argparse
import copy
from typing import List

import numpy as np
import pandas as pd
from pandas import DataFrame

from ar.edu.itba.simulacion.models.config import Config
from ar.edu.itba.simulacion.models.particle import Particle


def parse_arguments() -> Config:
    parser = argparse.ArgumentParser()
    parser.add_argument('-s', '--static_file', type=str, required=True)
    parser.add_argument('-d', '--dynamic_file', type=str, required=True)
    parser.add_argument('-v', '--va_time_file', type=str, required=True)
    parser.add_argument('-r', '--run_times', type=str, required=True)
    args = vars(parser.parse_args())

    return Config("." + args["static_file"],
                  "." + args["dynamic_file"],
                  "." + args["va_time_file"],
                  "." + args["run_times"],
                  0,
                  0)


def parse_particles(config: Config):
    particles = _parse_static_file(config)
    return _particles_frames(config.dynamic_file, particles,config.N)


def _parse_static_file(config: Config):
    static_file = open(config.static_file)
    n = int(static_file.readline())
    l = float(static_file.readline())
    particles = []

    for i in range(n):
        st_p = list(filter(lambda c: len(c) > 0, static_file.readline().split(" ")))
        particles.append(Particle(i, x=0, y=0, radius=float(st_p[0]), property=float(st_p[1]), angle=0, speed=0))
    static_file.close()

    config.L = l
    config.N = n

    return particles


def _particles_frames(dynamic_path: str, particles: List[Particle], number_of_particles: int):
    dynamic_file = open(dynamic_path)
    n = len(particles)

    iterations = 1000
    frames = []

    for i in range(iterations):
        t = int(dynamic_file.readline())
        p_frames = []
        for j in range(number_of_particles):
            dy_p = list(map(lambda p: float(p),dynamic_file.readline().split(" ")))
            p_frames.append(dy_p)
        df = pd.DataFrame(np.array(p_frames), columns=["id", "x", "y", "speed", "angle"])
        frames.append(df)

    return frames
