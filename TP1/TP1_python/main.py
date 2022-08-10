import sys

from models.config import Config
from utils.parser import parse_arguments, parse_input_particles, parse_output_file

from plots.plot import plot_dots


def main(config: Config):
    particles = parse_input_particles(config)
    neighbours = parse_output_file(config)

    plot_dots(particles, neighbours, config)


if __name__ == '__main__':
    config = parse_arguments()
    main(config)
