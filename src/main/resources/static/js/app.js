(function () {
  'use strict';

  var TONES = 6;
  var root = document.documentElement;

  /* ---------- helpers ---------- */
  function hash(text) {
    var h = 0;
    for (var i = 0; i < text.length; i++) {
      h = (h * 31 + text.charCodeAt(i)) >>> 0;
    }
    return h;
  }

  function toneClass(text) {
    return 'tone-' + (hash(text.toLowerCase()) % TONES);
  }

  function initials(name) {
    var parts = name.trim().split(/\s+/).filter(Boolean);
    if (parts.length === 0) { return '?'; }
    var first = parts[0].charAt(0);
    var last = parts.length > 1 ? parts[parts.length - 1].charAt(0) : '';
    return (first + last).toUpperCase();
  }

  /* ---------- dark mode toggle ---------- */
  var themeBtn = document.getElementById('theme-toggle');

  function syncThemeIcon() {
    if (!themeBtn) { return; }
    var icon = themeBtn.querySelector('.material-symbols-outlined');
    if (icon) { icon.textContent = root.classList.contains('dark') ? 'light_mode' : 'dark_mode'; }
  }

  if (themeBtn) {
    themeBtn.addEventListener('click', function () {
      root.classList.toggle('dark');
      try {
        localStorage.setItem('theme', root.classList.contains('dark') ? 'dark' : 'light');
      } catch (e) { /* storage unavailable: ignore */ }
      syncThemeIcon();
    });
  }
  syncThemeIcon();

  /* ---------- avatars and course pills get a colour from their text ---------- */
  document.querySelectorAll('.avatar[data-name]').forEach(function (el) {
    var name = el.getAttribute('data-name') || '';
    el.textContent = initials(name);
    el.classList.add(toneClass(name));
  });

  document.querySelectorAll('.pill').forEach(function (el) {
    el.classList.add(toneClass(el.textContent || ''));
  });

  /* ---------- dismissible alerts ---------- */
  function dismiss(alertEl) {
    if (!alertEl) { return; }
    alertEl.classList.add('hide');
    setTimeout(function () { alertEl.remove(); }, 250);
  }

  document.addEventListener('click', function (e) {
    var btn = e.target.closest('[data-dismiss]');
    if (btn) { dismiss(btn.closest('.alert')); }
  });

  document.querySelectorAll('.alert-success').forEach(function (el) {
    setTimeout(function () { dismiss(el); }, 6000);
  });

  /* ---------- delete confirmation modal ---------- */
  var modal = document.getElementById('delete-modal');

  if (modal) {
    var form = document.getElementById('delete-form');
    var nameEl = document.getElementById('delete-name');
    var numberEl = document.getElementById('delete-number');

    function openModal(button) {
      form.setAttribute('action', button.getAttribute('data-action'));
      nameEl.textContent = button.getAttribute('data-name');
      numberEl.textContent = button.getAttribute('data-number');
      modal.hidden = false;
    }

    function closeModal() {
      modal.hidden = true;
    }

    document.addEventListener('click', function (e) {
      var openBtn = e.target.closest('.btn-delete');
      if (openBtn) { openModal(openBtn); return; }
      if (e.target.closest('[data-close]') || e.target === modal) { closeModal(); }
    });

    document.addEventListener('keydown', function (e) {
      if (e.key === 'Escape' && !modal.hidden) { closeModal(); }
    });
  }
})();