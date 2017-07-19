SUMMARY = "General purpose cryptographic library based on the code from GnuPG"
HOMEPAGE = "http://directory.fsf.org/project/libgcrypt/"
BUGTRACKER = "https://bugs.g10code.com/gnupg/index"
SECTION = "libs"

# helper program gcryptrnd and getrandom are under GPL, rest LGPL
LICENSE = "GPLv2+ & LGPLv2.1+ & GPLv3+"
LICENSE_${PN} = "LGPLv2.1+"
LICENSE_${PN}-dev = "GPLv2+ & LGPLv2.1+"
LICENSE_dumpsexp-dev = "GPLv3+"

LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://COPYING.LIB;md5=bbb461211a33b134d42ed5ee802b37ff"

DEPENDS = "libgpg-error"

UPSTREAM_CHECK_URI = "https://gnupg.org/download/index.html"
SRC_URI = "${GNUPG_MIRROR}/libgcrypt/libgcrypt-${PV}.tar.gz \
           file://add-pkgconfig-support.patch \
           file://libgcrypt-fix-building-error-with-O2-in-sysroot-path.patch \
           file://fix-ICE-failure-on-mips-with-option-O-and-g.patch \
           file://fix-undefined-reference-to-pthread.patch \
"
SRC_URI[md5sum] = "36ace8f8dc4b1a82793ae759bbfc806f"
SRC_URI[sha256sum] = "0e72e91290d553c303095a50ea660279d8f11b76c6c86e826f470442fcdf1edd"

BINCONFIG = "${bindir}/libgcrypt-config"

inherit autotools texinfo binconfig-disabled pkgconfig

EXTRA_OECONF = "--disable-asm"

PACKAGECONFIG ??= "capabilities"
PACKAGECONFIG[capabilities] = "--with-capabilities,--without-capabilities,libcap"

do_configure_prepend () {
	# Else this could be used in preference to the one in aclocal-copy
	rm -f ${S}/m4/gpg-error.m4
}

# libgcrypt.pc is added locally and thus installed here
do_install_append() {
	install -d ${D}/${libdir}/pkgconfig
	install -m 0644 ${B}/src/libgcrypt.pc ${D}/${libdir}/pkgconfig/
}

PACKAGES =+ "dumpsexp-dev"

FILES_${PN}-dev += "${bindir}/hmac256"
FILES_dumpsexp-dev += "${bindir}/dumpsexp"

BBCLASSEXTEND = "native nativesdk"
