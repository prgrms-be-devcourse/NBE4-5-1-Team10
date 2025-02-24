import { NextResponse, type NextRequest } from "next/server";
import client from "./lib/backend/client";
import { cookies } from "next/headers";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";

export async function middleware(request: NextRequest) {
  // 클라이언트가 보낸 HttpOnly 쿠키
  const accessToken = request.cookies.get("accessToken");

  const { isLogin, isExpired } = parseAccessToken(accessToken);

  // 만료된 토큰 => 자동 재발급
  if (isLogin && isExpired) {
    return refreshAccessToken(request);
  }

  // 보호 라우트 접근 시 로그인 안 되어 있으면 401
  if (!isLogin && isProtectedRoute(request.nextUrl.pathname)) {
    return createUnauthorizedResponse();
  }

  return NextResponse.next();
}

async function refreshAccessToken(request: NextRequest) {
  const nextResponse = NextResponse.next();

  // refresh token으로 재발급
  const response = await client.POST("/api/v1/user/reissue", {
    headers: {
      cookie: request.headers.get("cookie") ?? "",
    },
  });

  // set-cookie 헤더를 가져와 응답에 설정
  const setCookieHeader = response.response.headers.getSetCookie();
  if (setCookieHeader) {
    nextResponse.headers.set("set-cookie", String(setCookieHeader));
  }
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
  // 보호해야 할 라우트 설정
  return pathname.startsWith("/cart");
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
