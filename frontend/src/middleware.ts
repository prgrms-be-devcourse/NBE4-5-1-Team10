import { NextResponse, type NextRequest } from "next/server";
import client from "./lib/backend/client";
import { cookies } from "next/headers";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";

export async function middleware(request: NextRequest) {
  const myCookies = await cookies();
  const accessToken = myCookies.get("accessToken");
  const { isLogin, isExpired } = parseAccessToken(accessToken);

  if (isLogin && isExpired) {
    return refreshAccessToken();
  }

  if (!isLogin && isProtectedRoute(request.nextUrl.pathname)) {
    return createUnauthorizedResponse();
  }
}

async function refreshAccessToken() {
  const nextResponse = NextResponse.next();

  const response = await client.POST("/api/v1/user/reissue", {
    headers: {
      cookie: (await cookies()).toString(),
    },
  });

  const cookie = response.response.headers.getSetCookie();

  nextResponse.headers.set("set-cookie", String(cookie));

  return nextResponse;
}

function parseAccessToken(accessToken: RequestCookie | undefined) {
  let isExpired = true;
  let payload = null;

  if (accessToken) {
    try {
      const tokenParts = accessToken.value.split(".");
      payload = JSON.parse(Buffer.from(tokenParts[1], "base64").toString());
      const expTimestamp = payload.exp * 1000;
      isExpired = Date.now() > expTimestamp;
    } catch (e) {
      console.error("토큰 파싱 중 오류 발생:", e);
    }
  }

  let isLogin = payload !== null;

  return { isLogin, isExpired, payload };
}
function isProtectedRoute(pathname: string): boolean {
  return (
    pathname.startsWith("/post/write") || pathname.startsWith("/post/edit")
  );
}

function createUnauthorizedResponse(): NextResponse {
  return new NextResponse("Login required.", {
    status: 401,
    headers: {
      "Content-Type": "text/html; charset=utf-8",
    },
  });
}

export const config = {
  matcher: "/((?!.*\\.|api\\/).*)",
};
