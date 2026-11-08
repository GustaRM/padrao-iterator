"""Gera o diagrama UML (estilo ferramenta de desenho, fundo quadriculado)."""
import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt
from matplotlib.patches import Rectangle, FancyArrowPatch

LH, PAD, NAME_H = 0.52, 0.25, 0.75
W_UN, H_UN = 62, 32

fig, ax = plt.subplots(figsize=(23.5, 12.2))
ax.set_xlim(0, W_UN); ax.set_ylim(0, H_UN); ax.set_aspect("equal"); ax.axis("off")
for i in range(W_UN + 1):
    ax.plot([i, i], [0, H_UN], color="#E3E3E3", lw=0.6, zorder=0)
for j in range(H_UN + 1):
    ax.plot([0, W_UN], [j, j], color="#E3E3E3", lw=0.6, zorder=0)

def box(cx, top, w, name, stereo=None, attrs=(), methods=()):
    h_name = NAME_H * (2 if stereo else 1) + (0.15 if stereo else 0)
    h_attr = max(len(attrs), 1) * LH + PAD
    h_meth = max(len(methods), 1) * LH + PAD
    h = h_name + h_attr + h_meth
    x0, y0 = cx - w / 2, top - h
    ax.add_patch(Rectangle((x0, y0), w, h, fc="white", ec="black", lw=1.4, zorder=2))
    ax.plot([x0, x0 + w], [top - h_name] * 2, color="black", lw=1.2, zorder=3)
    ax.plot([x0, x0 + w], [top - h_name - h_attr] * 2, color="black", lw=1.2, zorder=3)
    ty = top - 0.45
    if stereo:
        ax.text(cx, ty, f"«{stereo}»", ha="center", va="center", fontsize=9, zorder=4)
        ty -= 0.65
    ax.text(cx, ty, name, ha="center", va="center", fontsize=11, fontweight="bold", zorder=4)
    for k, a in enumerate(attrs):
        ax.text(x0 + 0.25, top - h_name - PAD / 2 - LH * (k + .5), a, ha="left", va="center", fontsize=8.5, zorder=4)
    for k, m in enumerate(methods):
        ax.text(x0 + 0.25, top - h_name - h_attr - PAD / 2 - LH * (k + .5), m, ha="left", va="center", fontsize=8.5, zorder=4)
    return dict(cx=cx, cy=(top + y0) / 2, top=top, bottom=y0, left=x0, right=x0 + w)

def line(pts, dashed=False):
    xs, ys = zip(*pts)
    ax.plot(xs, ys, color="black", lw=1.3, zorder=1, linestyle=(0, (5, 4)) if dashed else "-")

def arrow(p0, p1, style, dashed=False):
    a = FancyArrowPatch(p0, p1, arrowstyle=style, mutation_scale=22 if "|" in style else 18, lw=1.3,
                        color="black", zorder=1, shrinkA=0, shrinkB=0,
                        linestyle=(0, (5, 4)) if dashed else "-")
    if "|" in style:
        a.set_facecolor("white")
    ax.add_patch(a)

# ---------------- classes ----------------
main = box(8, 31.0, 5.6, "Main", None, (), ("+ main(args : String[])",))
aud = box(8, 26.4, 15, "Auditoria", None, (),
          ("+ contarTransacoesSuspeitas(extrato) : int  {static}",
           "+ contarTotalTransacoes(extrato) : int  {static}",
           "+ somarValores(extrato) : double  {static}",
           "+ somarValoresSuspeitos(extrato) : double  {static}",
           "+ ultimaTransacao(extrato) : Transacao  {static}"))

iterable = box(36, 31.2, 13, "Iterable<Transacao>", "interface", (), ("+ iterator() : Iterator<Transacao>",))
ext = box(36, 25.2, 16.5, "Extrato", None,
          ("- transacoes : List<Transacao>",),
          ("+ Extrato(transacoes : Transacao...)",
           "+ iterator() : Iterator<Transacao>",
           "+ reverso() : Iterable<Transacao>",
           "+ suspeitas() : Iterable<Transacao>"))
trans = box(55, 25.2, 11.5, "Transacao", None,
            ("- descricao : String", "- valor : double", "- suspeita : boolean"),
            ("+ getters / setters",))

it1 = box(22, 14.5, 12.6, "IteradorExtrato", None,
          ("- transacoes : List<Transacao>", "- indice : int"),
          ("+ hasNext() : boolean", "+ next() : Transacao"))
it2 = box(36, 14.5, 12.6, "IteradorExtratoReverso", None,
          ("- transacoes : List<Transacao>", "- indice : int"),
          ("+ hasNext() : boolean", "+ next() : Transacao"))
it3 = box(50, 14.5, 12.6, "IteradorFiltrado<T>", None,
          ("- origem : Iterator<T>", "- filtro : Predicate<T>", "- proximo : T"),
          ("+ hasNext() : boolean", "+ next() : T"))
its = [it1, it2, it3]

iterator = box(36, 6.6, 13, "Iterator<T>", "interface", (),
               ("+ hasNext() : boolean", "+ next() : T"))

# ---------------- relações ----------------
arrow((8, main["bottom"]), (8, aud["top"]), "->", dashed=True)                         # Main -> Auditoria
arrow((aud["right"], ext["top"] - 2.9), (ext["left"], ext["top"] - 2.9), "->", dashed=True)  # Auditoria -> Extrato

# Main -> Extrato (cria)
ym = main["cy"]
line([(main["right"], ym), (21, ym), (21, ext["top"] - 0.7)], dashed=True)
arrow((21, ext["top"] - 0.7), (ext["left"], ext["top"] - 0.7), "->", dashed=True)

# Extrato realiza Iterable
arrow((36, ext["top"]), (36, iterable["bottom"]), "-|>", dashed=True)

# Extrato -> Transacao (agregação, *)
ya = ext["top"] - 1.6
arrow((ext["right"], ya), (trans["left"], ya), "->")
ax.text(trans["left"] - 1.0, ya + 0.3, "*", fontsize=11)

# Extrato -> iteradores (cria)
for it, xs in zip(its, (30.5, 36, 41.5)):
    arrow((xs, ext["bottom"]), (it["cx"], it["top"]), "->", dashed=True)

# iteradores realizam Iterator<T>
ymid = (min(i["bottom"] for i in its) + iterator["top"]) / 2
for it in its:
    line([(it["cx"], it["bottom"]), (it["cx"], ymid)], dashed=True)
line([(it1["cx"], ymid), (it3["cx"], ymid)], dashed=True)
arrow((36, ymid), (36, iterator["top"]), "-|>", dashed=True)

# IteradorFiltrado -> Iterator<T> (origem, envolve outro iterator)
xr = 58.5
line([(it3["right"], it3["cy"]), (xr, it3["cy"]), (xr, iterator["cy"]), (iterator["right"] + 0.9, iterator["cy"])])
arrow((iterator["right"] + 0.9, iterator["cy"]), (iterator["right"], iterator["cy"]), "->")
ax.text(iterator["right"] + 0.2, iterator["cy"] + 0.5, "1", fontsize=9)

# legenda
ax.text(1.0, 12.4, "Papéis no padrão Iterator", fontsize=9.5, fontweight="bold")
leg = ["Aggregate: Extrato (Iterable)",
       "Iterator: Iterator<T> (interface do Java)",
       "ConcreteIterators: IteradorExtrato, IteradorExtratoReverso, IteradorFiltrado",
       "Client: Auditoria (usa for-each ou hasNext/next)"]
for k, t in enumerate(leg):
    ax.text(1.0, 11.5 - k * 0.8, f"• {t}", fontsize=8.5)

plt.savefig("diagrama-uml.png", dpi=140, bbox_inches="tight", facecolor="white")
