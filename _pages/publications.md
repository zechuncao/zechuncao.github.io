---
layout: archive
title: "Publications"
permalink: /publications/
---

{% include base_path %}

{% if site.googlescholar %}
  You can also find my articles on <u><a href="{{author.googlescholar}}">my Google Scholar profile</a>.</u>
{% endif %}

{% for post in site.publications reversed %}
  {% include archive-single.html %}
{% endfor %}

<!-- <sup>*</sup> Equal authorship statement -->