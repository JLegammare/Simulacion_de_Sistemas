import getopt
import sys
from typing import List

from models.config import Config
from models.particle import Particle

DEFAULT_N = 1
DEFAULT_L = 1
DEFAULT_M = 1
DEFAULT_RC = 1
DEFAULT_OUTPUT_FILE = ''
DEFAULT_DYNAMIC_FILE = ''
DEFAULT_STATIC_FILE = ''


def parse_arguments(argv: List[str]) -> Config:
    arguments = {
        'n_particles': DEFAULT_N,
        'n_cells': DEFAULT_M,
        'cell_length': DEFAULT_L,
        'radius': DEFAULT_RC,
        'dynamic_file': DEFAULT_DYNAMIC_FILE,
        'static_file': DEFAULT_DYNAMIC_FILE,
        'output_file': DEFAULT_OUTPUT_FILE

    }
    try:
        opts, args = getopt.getopt(argv, "l:n:m:r:o:s:d")
    except getopt.GetoptError:
        sys.exit(2)
    for opt, arg in opts:
        if opt in "-l":
            arguments['cell_length'] = float(arg)
        elif opt in "-n":
            arguments['n_particles'] = int(arg)
        elif opt in "-r":
            arguments['radius'] = float(arg)
        elif opt in "-m":
            arguments['n_cells'] = int(arg)
        elif opt in "-o":
            arguments['output_file'] = arg
        elif opt in "-s":
            arguments['static_file'] = arg
        elif opt in "-d":
            arguments['dynamic_file'] = arg

    return Config(arguments['cell_length'],
                  arguments['n_particles'],
                  arguments['radius'],
                  arguments['cell_length'],
                  arguments['static_file'],
                  arguments['dynamic_file'],
                  arguments['output_file']
                  )

# def parse_input_particles(static_path: str, dynamic_path: str) -> List:
#
#     particles = _parse_static_file(static_path)
#     return _parse_dynamic_file(dynamic_path,particles)

#
#
# def _parse_static_file(path: str) -> List:
#
#         static_file = open(path)
#         N = static_file.readline()
#         L = static_file.readline()
#
#
#     for i in range(int(N)):
#          st_p = static_file.readline().split(" ")
#          dy_p = static_file.readline().split(" ")
#
#          particles.append(Particle(i,))
#     static_file.close()
#     dynamic_file.close()
#
# def _parse_dynamic_file(dynamic_path: str, particles: List)-> List:
#     dynamic_file = open(dynamic_path)
#     n = len(particles)
#
#     for i in range(n):
#         dy_p = dynamic_file.readline().split(" ")
#
#
# def parse_output_file(file_path: str) -> List:
#     pass
