import numpy as np
import plotly.express as px
import pandas as pd

from models.config import Config
from utils.parser import parse_arguments

def main(config: Config):

    dfp = pd.read_csv(config.va_time_file, skiprows=1, sep=" ", names=["times","order_parameters"])
    df = pd.DataFrame(dict(
        Tiempo=dfp['times'],
        Va=dfp['order_parameters']
    ))
    fig = px.line(df, x="Tiempo", y="Va")
    data = list(range(0,20000+1,5000))
    tickvals = np.array(data)

    ticktext = [format_tick(t) for t in tickvals]

    ticktext[0]= "0"

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
        yaxis_range = [0,1.2]
    )
    fig.show()
def format_tick(n: int):
        m, e = f"{n:.0e}".split("e")
        m = n/_get_dec(n)
        e = int(e)
        return f"{m} x 10<sup>{e}</sup>"

def _get_dec(n:int):
    dec=10
    while n>=dec:
        dec=dec*10
    return dec/10



if __name__ == '__main__':
    conf = parse_arguments()
    main(conf)
