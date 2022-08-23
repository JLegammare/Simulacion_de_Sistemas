import argparse
from models.config import Config


def parse_arguments() -> Config:
    parser = argparse.ArgumentParser()
    parser.add_argument('-s', '--static_file', type=str, required=True)
    parser.add_argument('-d', '--dynamic_file', type=str, required=True)
    parser.add_argument('-v', '--va_time_file', type=str, required=True)
    parser.add_argument('-r', '--run_times', type=str, required=True)
    args = vars(parser.parse_args())

    return Config("." + args["static_file"],
                  "." + args["dynamic_file"],
                  "." + args["va_time_file"],
                  "." + args["run_times"]
                  )
