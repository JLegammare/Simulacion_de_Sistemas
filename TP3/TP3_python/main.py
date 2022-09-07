from models.config import Config
from plots.plot_particles import plot_dots
from utils.parser import parse_arguments, parse_input_particles, parse_output_file


def main(config: Config):
    particles = parse_input_particles(config)
    plot_dots(particles)


if __name__ == '__main__':
    conf = parse_arguments()
    main(conf)
