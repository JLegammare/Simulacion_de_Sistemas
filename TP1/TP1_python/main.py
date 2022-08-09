import sys
from typing import List

from TP1.TP1_python.models.particle import Particle
from TP1.TP1_python.utils.plot import plot_dots
from utils.argument_parser import parse_arguments

import matplotlib.pyplot as plt
plt.style.use('_mpl-gallery')


def main(arguments: dict):
    particles: List = [Particle(0, 0.1, 0.3, 0.4, 1.0),
                       Particle(1, 0.5, 0.5, 0.5, 1.5)]

    plot_dots(particles)


if __name__ == '__main__':
    arguments = parse_arguments(sys.argv[1:])
    main(arguments)

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
