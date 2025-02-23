import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  /* config options here */
  images: {
    domains: [
      "encrypted-tbn0.gstatic.com",
      "www.econgreen.co.kr",
      "groasting.com",
      "m.coffeegdero.com",
    ], // allow image domain
  },
};

export default nextConfig;
