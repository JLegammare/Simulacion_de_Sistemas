import getopt
import sys
from typing import List

DEFAULT_N = 1
DEFAULT_L = 1
DEFAULT_M = 1
DEFAULT_RC = 1
DEFAULT_OUTPUT_FILE = ''
DEFAULT_DYNAMIC_FILE = ''
DEFAULT_STATIC_FILE = ''


def parse_arguments(argv: List[str]) -> dict:
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
            arguments['cell_length'] = arg
        elif opt in "-n":
            arguments['n_particles'] = arg
        elif opt in "-r":
            arguments['radius'] = arg
        elif opt in "-m":
            arguments['n_cells'] = arg
        elif opt in "-o":
            arguments['output_file'] = arg
        elif opt in "-s":
            arguments['static_file'] = arg
        elif opt in "-d":
            arguments['dynamic_file'] = arg

    return arguments
