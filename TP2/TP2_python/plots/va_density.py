import statistics
import pandas as pd
import plotly.graph_objects as go

def main():

    inc = 100
    vias = 5000
    L= 20

    data = []
    x = []
    avgs = []
    stdevs_list = []

    for i in range(1,17):
        number_of_particles = i * inc
        x.append(number_of_particles/pow(L,2))
        dfp = pd.read_csv("./va_density_files/VaTime{it}.txt".format(it=number_of_particles), skiprows=1, sep=" ", names=["times","order_parameters"])
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
            xaxis=dict(title='Densidad'),
            yaxis=dict(title='Va Promedio'),
            font=dict(
                family="Arial",
                size=22,
            ),
        )
    )

    fig.show()


if __name__ == '__main__':
    main()
