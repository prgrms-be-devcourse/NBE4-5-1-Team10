import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  /* config options here */
  images: {
    remotePatterns: [
      {
        protocol: "https",
        hostname: "encrypted-tbn0.gstatic.com",
      },
      {
        protocol: "https",
        hostname: "www.econgreen.co.kr",
      },
      {
        protocol: "https",
        hostname: "groasting.com",
      },
      {
        protocol: "https",
        hostname: "m.coffeegdero.com",
      },
    ], // allow image domain
  },
};

export default nextConfig;
