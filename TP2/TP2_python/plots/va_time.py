import numpy as np
import plotly.express as px
import pandas as pd

from models.config import Config
from utils.parser import parse_arguments

def main(config: Config):

    dfp = pd.read_csv(config.va_time_file, skiprows=1, sep=" ", names=["times","order_parameters"])
    df = pd.DataFrame(dict(
        iteraciones=dfp['times'],
        Va=dfp['order_parameters']
    ))
    fig = px.line(df, x="iteraciones", y="Va")
    data = list(range(0,20000+1,5000))
    tickvals = np.array(data,dtype=float)

    ticktext = [format_tick(t) for t in tickvals]

    fig.update_layout(
        xaxis = dict(
            tickmode = 'array',
            tickvals = tickvals,
            ticktext = ticktext,
        ),
        font=dict(
            family="Arial",
            size=40,
            ),
        yaxis_range = [0,1]
    )
    fig.show()
def format_tick(n: float):
        m, e = f"{n:.0e}".split("e")
        e = int(e)
        return f"{m} x 10<sup>{e}</sup>"


if __name__ == '__main__':
    conf = parse_arguments()
    main(conf)
