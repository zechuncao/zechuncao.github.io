// Animated Exam 1 distribution. State 0 = raw scores, 1 = after the curve,
// 2 = after the curve and EC1. Each click on the slide reveals one empty
// .curve-step fragment, and the chart animates to the matching state.
(function () {
  const D = window.EXAM1;
  const NS = "http://www.w3.org/2000/svg";
  const LO = 10, X0 = 150, X1 = 975, TOP = 55, BASE = 330;
  const BANDS = [[LO, 60, "below 60", "#fdeeee"], [60, 70, "60s", "#fff5e3"],
                 [70, 80, "70s", "#eef7ee"], [80, 90, "80s", "#e7f2f8"],
                 [90, 100, "90+", "#eeebf7"]];
  const CAPTIONS = ["Raw Exam 1 scores (the \u201cbefore\u201d picture)",
                    "After the curve: everybody scoots to the right",
                    "After the curve and EC1: your extra credit at work"];
  const MS_PER_STEP = 1800;

  const ymax = 1.08 * Math.max(...D.steps.flat().flat());
  const sx = s => X0 + (X1 - X0) * (s - LO) / (100 - LO);
  const sy = y => BASE - (BASE - TOP) * y / ymax;

  let svg, caption, liveFill, live, ghostRaw, ghostCurved, legend, meanLine, meanText, counts;
  let shown = 0, anim = null;

  function el(name, attrs, parent, text) {
    const e = document.createElementNS(NS, name);
    for (const k in attrs) e.setAttribute(k, attrs[k]);
    if (text !== undefined) e.textContent = text;
    (parent || svg).appendChild(e);
    return e;
  }

  // Open line along the curve; the filled area closes it along the axis.
  function linePath(ys) {
    let d = "";
    for (let s = LO; s <= 100; s++) d += `${s === LO ? "M" : "L"}${sx(s).toFixed(1)},${sy(ys[s]).toFixed(1)}`;
    return d;
  }
  const areaPath = ys => `M${sx(LO)},${BASE}L` + linePath(ys).slice(1) + `L${sx(100)},${BASE}Z`;

  // Curve at a fractional state in [0, 2], interpolating between stored frames.
  function curveAt(x) {
    const step = Math.min(1, Math.floor(x)), t = x - step;
    const fr = D.steps[step], f = t * (fr.length - 1);
    const i = Math.floor(f), j = Math.min(i + 1, fr.length - 1), u = f - i;
    return fr[i].map((y, k) => y + u * (fr[j][k] - y));
  }

  function build() {
    BANDS.forEach(([a, b, label, fill]) => {
      el("rect", {x: sx(a), y: TOP - 10, width: sx(b) - sx(a), height: BASE - TOP + 10, fill: fill});
      el("text", {x: (sx(a) + sx(b)) / 2, y: 392, class: "band-label"}, null, label);
    });
    for (let s = LO; s <= 100; s += 10) {
      el("line", {x1: sx(s), x2: sx(s), y1: BASE, y2: BASE + 8, class: "axis"});
      el("text", {x: sx(s), y: BASE + 32, class: "tick"}, null, s);
    }
    el("line", {x1: sx(LO), x2: sx(100), y1: BASE, y2: BASE, class: "axis"});
    el("text", {x: X0 - 18, y: BASE + 32, class: "row-label"}, null, "score");
    el("text", {x: X0 - 18, y: 392, class: "row-label"}, null, "range");
    el("text", {x: X0 - 18, y: 432, class: "row-label"}, null, "students");
    counts = BANDS.map(([a, b]) => el("text", {x: (sx(a) + sx(b)) / 2, y: 434, class: "band-count"}));

    ghostRaw = el("path", {d: linePath(curveAt(0)), class: "ghost ghost-raw"});
    ghostCurved = el("path", {d: linePath(curveAt(1)), class: "ghost ghost-curved"});
    liveFill = el("path", {class: "live-fill"});
    live = el("path", {class: "live"});
    meanLine = el("line", {y1: TOP - 10, y2: BASE, class: "mean-line"});
    meanText = el("text", {y: TOP - 18, class: "mean-text"});

    legend = el("g", {class: "legend"});
    el("line", {x1: X0 + 12, x2: X0 + 52, y1: TOP + 12, y2: TOP + 12, class: "ghost-raw"}, legend);
    el("text", {x: X0 + 62, y: TOP + 19}, legend, "raw");
    const second = el("g", {class: "legend-curved"}, legend);
    el("line", {x1: X0 + 12, x2: X0 + 52, y1: TOP + 44, y2: TOP + 44, class: "ghost-curved"}, second);
    el("text", {x: X0 + 62, y: TOP + 51}, second, "curved");
    // The solid line is whatever the chart shows now: "curved" on the second
    // row after the first click, "curved + EC1" on the third after the second.
    const third = el("g", {class: "legend-final"}, legend);
    el("line", {x1: X0 + 12, x2: X0 + 52, y1: TOP + 76, y2: TOP + 76, class: "live"}, third);
    el("text", {x: X0 + 62, y: TOP + 83}, third);
  }

  function draw(x) {
    const ys = curveAt(x);
    liveFill.setAttribute("d", areaPath(ys));
    live.setAttribute("d", linePath(ys));
    const step = Math.min(1, Math.floor(x)), t = x - step;
    const m = D.means[step] + t * (D.means[step + 1] - D.means[step]);
    meanLine.setAttribute("x1", sx(m));
    meanLine.setAttribute("x2", sx(m));
    meanText.setAttribute("x", sx(m));
    meanText.textContent = `average ${m.toFixed(1)}`;
    ghostRaw.style.opacity = x > 0 ? 1 : 0;
    ghostCurved.style.opacity = x > 1 ? 1 : 0;
    legend.style.opacity = x > 0 ? 1 : 0;
    legend.querySelector(".legend-curved").style.opacity = x > 1 ? 1 : 0;
    const solid = legend.querySelector(".legend-final");
    solid.setAttribute("transform", x > 1 ? "" : "translate(0,-32)");
    solid.querySelector("text").textContent = x > 1 ? "curved + EC1" : "curved";
    const k = Math.floor(x + 1e-9);
    counts.forEach((c, i) => { c.textContent = D.counts[k][i]; });
  }

  function animateTo(target) {
    if (anim) cancelAnimationFrame(anim);
    caption.textContent = CAPTIONS[target];
    const from = shown, start = performance.now();
    const dur = Math.abs(target - from) * MS_PER_STEP;
    const ease = u => (u < 0.5 ? 2 * u * u : 1 - Math.pow(-2 * u + 2, 2) / 2);
    function tick(now) {
      const u = dur ? Math.min(1, (now - start) / dur) : 1;
      shown = from + (target - from) * ease(u);
      draw(shown);
      if (u < 1) anim = requestAnimationFrame(tick);
      else { shown = target; draw(shown); anim = null; }
    }
    anim = requestAnimationFrame(tick);
  }

  const stepsShown = slide =>
    slide.querySelectorAll(".curve-step.visible").length;

  function onSlide(e) {
    if (!e.currentSlide.contains(svg)) return;
    if (anim) cancelAnimationFrame(anim);
    shown = stepsShown(e.currentSlide);
    caption.textContent = CAPTIONS[shown];
    draw(shown);
  }

  function onFragment(e) {
    if (!e.fragment.classList.contains("curve-step")) return;
    animateTo(stepsShown(Reveal.getCurrentSlide()));
  }

  function init() {
    svg = document.getElementById("curve-chart");
    caption = document.getElementById("curve-caption");
    if (!svg) return;
    build();
    shown = Reveal.isPrintView() ? 2 : 0;
    caption.textContent = CAPTIONS[shown];
    draw(shown);
    Reveal.on("slidechanged", onSlide);
    Reveal.on("fragmentshown", onFragment);
    Reveal.on("fragmenthidden", onFragment);
    if (!Reveal.isPrintView()) onSlide({currentSlide: Reveal.getCurrentSlide()});
  }

  if (Reveal.isReady()) init(); else Reveal.on("ready", init);
})();
