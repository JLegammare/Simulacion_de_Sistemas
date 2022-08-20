from ovito.io import export_file
from ovito.pipeline import Pipeline

from models.config import Config
from utils.parser import parse_arguments, parse_input_particles, parse_output_file


def main(config: Config):
    particles = parse_input_particles(config)
    neighbours = parse_output_file(config)

    # print(particles)
    #
    # pipeline = Pipeline(particles)
    #
    # export_file(pipeline, './assets/Output.txt', 'lammps/dump',
    #             columns=["Particle Identifier", "Position.X", "Position.Y", "Position.Z", "Radius", "Neighbor"])


if __name__ == '__main__':
    conf = parse_arguments()
    main(conf)
