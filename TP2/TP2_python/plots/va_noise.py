import plotly.express as px
import pandas as pd

from models.config import Config
from utils.parser import parse_arguments

def main(config: Config):

    dfp = pd.read_csv(config.va_time_file, skiprows=1, sep=" ", names=["times","order_parameters"])
    df = pd.DataFrame(dict(
        x=dfp['times'],
        y=dfp['order_parameters']
    ))
    fig = px.line(df, x="x", y="y", title="Order Parameter vs Time")
    fig.show()


if __name__ == '__main__':
    conf = parse_arguments()
    main(conf)
