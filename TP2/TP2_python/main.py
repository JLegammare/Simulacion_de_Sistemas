from models.config import Config
from utils.parser import parse_arguments, parse_particles


def main(config: Config):

    particles_frames = parse_particles(config)
    a = 0

    # pipeline = import_file("./assets/Output.txt", columns=["Particle Id", "Position.X", "Position.Y","Position.Z", "Velocity", "Omega"])
    # keys = pipeline.compute().particles.keys()
    # export_file(pipeline, './assets/results.xyz', 'lammps/dump',
    #             columns=["Particle Identifier", "Position.X", "Position.Y", "Position.Z", "Velocity", "Omega"],
    #             multiple_frames=True, start_frame=0,end_frame=99)


if __name__ == '__main__':
    conf = parse_arguments()
    main(conf)
