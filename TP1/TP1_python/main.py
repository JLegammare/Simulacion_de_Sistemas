from ar.edu.itba.simulacion.models.config import Config
from plots.plot import plot_dots
from ar.edu.itba.simulacion.models.utils.parser import parse_arguments, parse_input_particles, parse_output_file


def main(config: Config):
    particles = parse_input_particles(config)
    neighbours = parse_output_file(config)

    plot_dots(particles, neighbours, config)


if __name__ == '__main__':
    conf = parse_arguments()
    main(conf)
