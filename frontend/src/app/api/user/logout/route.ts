import { cookies } from "next/headers";
import { NextRequest, NextResponse } from "next/server";

export async function POST(req: NextRequest) {
  try {
    const accessToken = (await cookies()).get("accessToken");
    if (!accessToken) {
      return NextResponse.json({ error: "No access token" }, { status: 401 });
    }

    const refreshToken = (await cookies()).get("refresh");
    if (!refreshToken) {
      return NextResponse.json({ error: "No refresh token" }, { status: 401 });
    }

    const logoutResponse = await fetch(
      "http://localhost:8080/api/v1/user/logout",
      {
        method: "POST",
        headers: {
          Authorization: `Bearer ${accessToken.value}`,
          cookie: `refresh=${refreshToken.value}`,
          "Content-Type": "application/json",
        },
        credentials: "include",
      }
    );

    if (!logoutResponse.ok) {
      return NextResponse.json({ error: "Failed to log out" }, { status: 401 });
    }

    const response = NextResponse.json({ success: true });

    response.cookies.set("accessToken", "", {
      expires: new Date(0),
      path: "/",
    });
    response.cookies.set("refresh", "", {
      expires: new Date(0),
      path: "/",
    });

    return response;
  } catch (err) {
    console.error("Logout route error:", err);
    return NextResponse.json({ error: "Server error" }, { status: 500 });
  }
}
