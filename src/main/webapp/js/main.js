(function ($) {

	/**********************************
	* CUSTOMIZE THE DEFAULT SETTINGS
	* Ex:
	* var _settings = {
	* 	id: 'modal',
	* 	src: function(sender){
	*		return jQuery(sender).attr('href');
	*	},
	* 	width: 800,
	* 	height: 600
	* }
	**********************************/
	var _settings3 = {
		width: 800, // Use this value if not set in CSS or HTML
		height: 600, // Use this value if not set in CSS or HTML
		overlayOpacity: .85, // Use this value if not set in CSS or HTML
		id: 'modal',
		fadeInSpeed: 0,
		fadeOutSpeed: 0,
		src: function (sender) {
			return jQuery(sender).attr('href');
		},
		redirect: null
	};

	/**********************************
	* DO NOT CUSTOMIZE BELOW THIS LINE
	**********************************/
	$.modal3 = function (options) {
		return _modal3(this, options);
	};
	$.modal3.open = function () {
		_modal3.open();
	};
	$.modal3.close = function () {
		_modal3.close();
	};
	$.fn.modal3 = function (options) {
		return _modal3(this, options);
	};
	_modal3 = function (sender, params) {
		this.options = {
			parent: null,
			overlayOpacity: null,
			id: null,
			content: null,
			width: null,
			height: null,
			message: false,
			modalClassName: null,
			imageClassName: null,
			closeClassName: null,
			overlayClassName: null,
			src: null,
			redirect: null
		};
		this.options = $.extend({}, options, _defaults3);
		this.options = $.extend({}, options, _settings3);
		this.options = $.extend({}, options, params);
		this.close = function () {
			jQuery('.' + options.modalClassName + ', .' + options.overlayClassName).fadeOut(_settings3.fadeOutSpeed, function () { jQuery(this).unbind().remove(); });
		};
		this.open = function () {
			if (typeof options.src == 'function') {
				options.src = options.src(sender);
			} else {
				options.src = options.src || _defaults3.src(sender);
			}

			var fileExt = /^.+\.((jpg)|(gif)|(jpeg)|(png)|(jpg))$/i;
			var contentHTML = '';
			if (fileExt.test(options.src)) {
				contentHTML = '<div class="' + options.imageClassName + '"><img src="' + options.src + '"/></div>';

			} else {
				contentHTML = '<iframe width="' + options.width + '" height="' + options.height + '" frameborder="0" scrolling="yes" allowtransparency="true" src="' + options.src + '"></iframe>';
			}
			options.content = options.content || contentHTML;

			if (jQuery('.' + options.modalClassName).length && jQuery('.' + options.overlayClassName).length) {
				jQuery('.' + options.modalClassName).html(options.content);
			} else {
				$overlay = jQuery((_isIE63()) ? '<iframe src="BLOCKED SCRIPT\'<html></html>\';" scrolling="yes" frameborder="0" class="' + options.overlayClassName + '"></iframe><div class="' + options.overlayClassName + '"></div>' : '<div class="' + options.overlayClassName + '"></div>');
				$overlay.hide().appendTo(options.parent);

				$modal = jQuery('<div id="' + options.id + '" class="' + options.modalClassName + '" style="width:' + options.width + 'px; height:' + options.height + 'px; margin-top:-' + (options.height / 2) + 'px; margin-left:-' + (options.width / 2) + 'px;">' + options.content + '</div>');
				$modal.hide().appendTo(options.parent);

				$close = jQuery('<a class="' + options.closeClassName + '"></a>');
				$close.appendTo($modal);

				var overlayOpacity = _getOpacity3($overlay.not('iframe')) || options.overlayOpacity;
				$overlay.fadeTo(0, 0).show().not('iframe').fadeTo(_settings3.fadeInSpeed, overlayOpacity);
				$modal.fadeIn(_settings3.fadeInSpeed);
				
				if(options.redirect!=null){
					$close.click(function () { location.href=options.redirect; });
					$overlay.click(function () { location.href=options.redirect; });
				}else{
					$close.click(function () { jQuery.modal().close()});
					$overlay.click(function () { jQuery.modal().close(); });
				}
				
			}
		};
		return this;
	};
	_isIE63 = function () {
		if (document.all && document.getElementById) {
			if (document.compatMode && !window.XMLHttpRequest) {
				return true;
			}
		}
		return false;
	};
	_getOpacity3 = function (sender) {
		$sender = jQuery(sender);
		opacity = $sender.css('opacity');
		filter = $sender.css('filter');

		if (filter.indexOf("opacity=") >= 0) {
			return parseFloat(filter.match(/opacity=([^)]*)/)[1]) / 100;
		}
		else if (opacity != '') {
			return opacity;
		}
		return '';
	};
	_defaults3 = {
		parent: 'body',
		overlayOpacity: 85,
		id: 'modal',
		content: null,
		width: 800,
		height: 600,
		modalClassName: 'modal-window',
		imageClassName: 'modal-image',
		closeClassName: 'close-window',
		overlayClassName: 'modal-overlay',
		src: function (sender) {
			return jQuery(sender).attr('href');
		},
		redirect: null
	};
})(jQuery);