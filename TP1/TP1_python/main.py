import sys
from typing import List

from models.config import Config
from models.particle import Particle
from utils.parser import parse_arguments

from utils.plot import plot_dots


def main(config: Config):
    particles: List = [Particle(0, 0.1, 0.3, 10, 1.0),
                       Particle(1, 0.5, 0.5, 10, 1.5)]
    neighbours: List = [Particle(2, 1.1, 1.3, 10, 1.0),
                        Particle(3, 1.5, 1.5, 10, 1.5)]

    # parse_input_particles(config.static_file,config.dynamic_file)
    # parse_output_file(config.static_file)

    plot_dots(particles, neighbours, config)


if __name__ == '__main__':
    config = parse_arguments(sys.argv[1:])
    main(config)
