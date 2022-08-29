import statistics
import pandas as pd
import plotly.graph_objects as go

def main():

    inc = 0.1
    densidad = 1
    vias = 5000

    data = []
    x = []
    avgs = []
    stdevs_list = []

    for i in range(0,50+1):

        x.append(i*0.1)
        dfp = pd.read_csv("./va_time_files/VaTime{it}.txt".format(it=i), skiprows=1, sep=" ", names=["times","order_parameters"])
        l= list(dfp.get("order_parameters"))
        end = len(l)
        its_list = l[vias:end]
        avgs.append(sum(its_list)/len(its_list))
        stdevs_list.append(statistics.stdev(its_list))

    data.append(
        go.Scatter(
            x=x,
                y=avgs,
                mode='markers+lines',
                error_y=dict(array=stdevs_list)
            )
        )

    fig = go.Figure(
        data = data,
        layout= go.Layout(
            xaxis=dict(title='Ruido'),
            yaxis=dict(title='Va Promedio'),
            font=dict(
                family="Arial",
                size=22,
            ),
            legend=dict(
                yanchor="top",
                y=0.99,
                xanchor="right",
                x=0.99
            )
        )
    )

    fig.show()


if __name__ == '__main__':
    main()
