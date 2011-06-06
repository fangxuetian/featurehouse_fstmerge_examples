import matplotlib.pyplot as plt
import mpl_toolkits.axisartist as AA
fig = plt.figure(1)
fig.subplots_adjust(right=0.85)
ax = AA.Subplot(fig, 1, 1, 1)
fig.add_subplot(ax)
ax.axis["bottom", "top", "right"].set_visible(False)
ax.axis["y=0"] = ax.new_floating_axis(nth_coord=0, value=0,
                                      axis_direction="bottom")
ax.axis["y=0"].toggle(all=True)
ax.axis["y=0"].label.set_text("y = 0")
ax.set_ylim(-2, 4)
plt.show()
