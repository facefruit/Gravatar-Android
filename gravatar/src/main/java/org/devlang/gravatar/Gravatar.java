package org.devlang.gravatar;

public class Gravatar {
    private static final String GRAVATAR_HTTP_URL = "http://www.gravatar.com/avatar/";
    private static final String GRAVATAR_HTTPS_URL = "https://www.gravatar.com/avatar/";

    public static final String RATING_G = "g";
    public static final String RATING_PG = "pg";
    public static final String RATING_R = "r";
    public static final String RATING_X = "x";
    public static final String IMAGE_DEFAULT = "";
    public static final String MAGE_404 = "404";
    public static final String IMAGE_MP = "mp";
    public static final String IMAGE_IDENTICON = "identicon";
    public static final String IMAGE_MONSTERID = "monsterid";
    public static final String IMAGE_WAVATAR = "wavatar";
    public static final String IMAGE_RETRO = "retro";
    public static final String IMAGE_ROBOHASH = "robohash";
    public static final String IMAGE_BLANK = "blank";

    private long size;
    private String email;
    private String defaultImage;
    private boolean forceDefault;
    private String rating;
    private boolean secureRequest;
    private String gravatarUrl;

    private Gravatar(Builder builder) {
        size = builder.size;
        email = builder.email;
        defaultImage = builder.defaultImage;
        forceDefault = builder.forceDefault;
        rating = builder.rating;
        secureRequest = builder.secureRequest;
        gravatarUrl = builder.gravatarUrl;
    }

    public long getSize() {
        return size;
    }

    public String getEmail() {
        return email;
    }

    public String getDefaultImage() {
        return defaultImage;
    }

    public boolean isForceDefault() {
        return forceDefault;
    }

    public String getRating() {
        return rating;
    }

    public boolean isSecureRequest() {
        return secureRequest;
    }

    public String getGravatarUrl() {
        return gravatarUrl;
    }

    public static class Builder {
        private long size = GravatarManager.getDefaultSize();
        private String email;
        private String defaultImage = GravatarManager.getDefaultImage();
        private boolean forceDefault = GravatarManager.isForceDefault();
        private String rating = GravatarManager.getRating();
        private boolean secureRequest = GravatarManager.isSecureRequest();
        private String gravatarUrl;

        public Builder(String email) {
            this.email = email;
        }


        /**
         * <h3>E-mail</h3>
         * <p>
         * Gravatar images may be requested just like a normal image, using an IMG tag.
         * To get an image specific to a user, you must first calculate their email hash.
         * <p>
         * The most basic image request URL looks like this:
         * <p>
         * https://www.gravatar.com/avatar/HASH
         * <p>
         * where HASH is replaced with the calculated hash for the specific email address
         * you are requesting. For example, here is my base URL:
         * <p>
         * https://www.gravatar.com/avatar/205e460b479e2e5b48aec07710c08d50
         * <p>
         * When wrapped in an IMG tag, that URL will produce:
         *
         * <img src="https://www.gravatar.com/avatar/205e460b479e2e5b48aec07710c08d50" />
         * <p>
         * If you require a file-type extension (some places do) then you may also add
         * an (optional) .jpg extension to that URL:
         * <p>
         * https://www.gravatar.com/avatar/205e460b479e2e5b48aec07710c08d50.jpg
         *
         * @param email
         * @return
         */
        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        /**
         * <h3>Size</h3>
         * By default, images are presented at 80px by 80px if no size parameter is supplied.
         * You may request a specific image size, which will be dynamically delivered from Gravatar
         * by using the s= or size= parameter and passing a single pixel dimension
         * (since the images are square):
         * <p>
         * <b><i>https://www.gravatar.com/avatar/205e460b479e2e5b48aec07710c08d50?s=200</i></b>
         * <p>
         * You may request images anywhere from 1px up to 2048px,
         * however note that many users have lower resolution images,
         * so requesting larger sizes may result in pixelation/low-quality images.
         *
         * @param size from 1px up tp 2048px
         * @return
         */
        public Builder setSize(long size) {
            this.size = size;
            return this;
        }

        /**
         * <h3>Default Image</h3>
         * What happens when an email address has no matching Gravatar image? By default, this:
         * <p>
         * If you'd prefer to use your own default image (perhaps your logo, a funny face, whatever),
         * then you can easily do so by supplying the URL to an image in the d= or default= parameter.
         * The URL should be URL-encoded to ensure that it carries across correctly, for example:
         *
         * <img src="https://www.gravatar.com/avatar/00000000000000000000000000000000?d=https%3A%2F%2Fexample.com%2Fimages%2Favatar.jpg" />
         * <p>
         * To URL-encode a string in PHP, you can use something like this:
         * <p>
         * cho urlencode( 'https://example.com/images/avatar.jpg' );
         * <p>
         * When you include a default image, Gravatar will automatically serve up that image if
         * there is no image associated with the requested email hash.
         * There are a few conditions which must be met for default image URL:
         * <p>
         * 1. MUST be publicly available (e.g. cannot be on an intranet, on a local development machine,
         * behind HTTP Auth or some other firewall etc). Default images are passed
         * through a security scan to avoid malicious content.
         * 2. MUST be accessible via HTTP or HTTPS on the standard ports, 80 and 443, respectively.
         * 3. MUST have a recognizable image extension (jpg, jpeg, gif, png)
         * M4. UST NOT include a querystring (if it does, it will be ignored)
         * In addition to allowing you to use your own image, Gravatar has a number of
         * built in options which you can also use as defaults. Most of these work by
         * taking the requested email hash and using it to generate a themed image that is
         * unique to that email address. To use these options, just pass one of
         * the following keywords as the d= parameter to an image request:
         * <p>
         * <b>404</b>: do not load any image if none is associated with the email hash,
         * instead return an HTTP 404 (File Not Found) response
         * <p>
         * <b>mp</b>: (mystery-person) a simple, cartoon-style silhouetted outline of
         * a person (does not vary by email hash)
         * <p>
         * <b>identicon</b>: a geometric pattern based on an email hash
         * <p>
         * <b>monsterid</b>: a generated 'monster' with different colors, faces, etc
         * <p>
         * <b>wavatar</b>: generated faces with differing features and backgrounds
         * <p>
         * <b>retro</b>: awesome generated, 8-bit arcade-style pixelated faces
         * <p>
         * <b>robohash</b>: a generated robot with different colors, faces, etc
         * <p>
         * <b>blank</b>: a transparent PNG image (border added to HTML below for demonstration purposes)
         *
         * @param defaultImage
         * @return
         */
        public Builder setDefaultImage(String defaultImage) {
            this.defaultImage = defaultImage;
            return this;
        }

        /**
         * <h3>Force Default</h3>
         * If for some reason you wanted to force the default image to always load,
         * you can do that by using the f= or forcedefault= parameter, and setting its value to y.
         * <p>
         * https://www.gravatar.com/avatar/205e460b479e2e5b48aec07710c08d50?f=y
         *
         * @param forceDefault
         * @return
         */
        public Builder setForceDefault(boolean forceDefault) {
            this.forceDefault = forceDefault;
            return this;
        }

        /**
         * <h3>Rating</h3>
         * Gravatar allows users to self-rate their images so that they can indicate
         * if an image is appropriate for a certain audience. By default,
         * only 'G' rated images are displayed unless you indicate
         * that you would like to see higher ratings. Using the r= or rating= parameters,
         * you may specify one of the following ratings to request images
         * up to and including that rating:
         * <p>
         * <b>g</b>: suitable for display on all websites with any audience type.
         * <p>
         * <b>pg</b>: may contain rude gestures, provocatively dressed individuals,
         * the lesser swear words, or mild violence.
         * <p>
         * <b>r</b>: may contain such things as harsh profanity, intense violence, nudity, or hard drug use.
         * <p>
         * <b>x</b>: may contain hardcore sexual imagery or extremely disturbing violence.
         * <p>
         * If the requested email hash does not have an image meeting the requested rating level,
         * then the default image is returned (or the specified default, as per above)
         * <p>
         * To allow images rated G or PG use something like this:
         * <p>
         * https://www.gravatar.com/avatar/205e460b479e2e5b48aec07710c08d50?r=pg
         *
         * @param rating
         * @return
         */
        public Builder setRating(String rating) {
            this.rating = rating;
            return this;
        }

        /**
         * <h3>Secure Requests</h3>
         * As you may have noticed, all of the above example URLs start with HTTPS.
         * You don't need to do anything special to load Gravatars on a secure page,
         * just make sure your Gravatar URLs start with 'https' (or you can use
         * the 'protocol-agnostic' approach of starting the URLs with '//'
         * which will automatically use 'https:' on a secure page, or 'http:'
         * on an insecure one).
         *
         * @param secureRequest
         * @return
         */
        public Builder setSecureRequest(boolean secureRequest) {
            this.secureRequest = secureRequest;
            return this;
        }

        public Gravatar build() {
            StringBuffer sb = new StringBuffer(secureRequest ? GRAVATAR_HTTPS_URL : GRAVATAR_HTTP_URL);
            sb.append(Md5Util.md5Hex(email));
            sb.append("?s=").append(size);
            sb.append("&d=").append(defaultImage);
            sb.append("&f=").append(forceDefault ? "y" : "n");
            sb.append("&r=").append(rating);
            gravatarUrl = sb.toString();
            return new Gravatar(this);
        }
    }
}
