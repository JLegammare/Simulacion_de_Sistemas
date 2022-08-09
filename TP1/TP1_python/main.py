import sys

from models.config import Config
from utils.parser import parse_arguments, parse_input_particles, parse_output_file

from utils.plot import plot_dots


def main(config: Config):
    particles = parse_input_particles(config.static_file,config.dynamic_file)
    neighbours = parse_output_file(config.output_file,config.N)

    print(plot_dots(particles, neighbours, config))


if __name__ == '__main__':
    config = parse_arguments(sys.argv[1:])
    main(config)
