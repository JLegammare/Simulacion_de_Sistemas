import statistics
import pandas as pd
import plotly.graph_objects as go


def main():
    step = 0.1
    vias = 5000

    it_dic = {
        1:"N=40",
        2:"N=100",
        3:"N=400",
    }

    data2 = []

    for j in range(1, 4):
        x = []
        avgs = []
        stdevs_list = []
        data = []

        for i in range(0, 50 + 1):
            x.append(i * step)
            dfp = pd.read_csv("./va_time_files/{n}/VaTime{it}.txt".format(it=i, n=j), skiprows=1, sep=" ",
                              names=["times", "order_parameters"])
            l = list(dfp.get("order_parameters"))
            end = len(l)
            its_list = l[vias:end]
            avgs.append(sum(its_list) / len(its_list))
            stdevs_list.append(statistics.stdev(its_list))

        data.append(
            go.Scatter(
                x=x,
                y=avgs,
                mode='markers+lines',
                error_y=dict(array=stdevs_list),
            )
        )
        data2.append(
            go.Scatter(
                x=x,
                y=avgs,
                mode='markers+lines',
                name=it_dic[j]
            )
        )
        fig = go.Figure(
            data=data,
            layout=go.Layout(
                xaxis=dict(title='Ruido'),
                yaxis=dict(title='Va Promedio'),
                font=dict(
                    family="Arial",
                    size=30,
                ),
            )
        )
        fig.show()

    fig2 = go.Figure(
        data=data2,
        layout=go.Layout(
            xaxis=dict(title='Ruido'),
            yaxis=dict(title='Va Promedio'),
            font=dict(
                family="Arial",
                size=22,
            ),
        )
    )

    fig2.show()


if __name__ == '__main__':
    main()
