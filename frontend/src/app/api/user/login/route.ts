import { NextRequest, NextResponse } from "next/server";

export async function POST(req: NextRequest) {
  try {
    const body = await req.json();
    const { email, password } = body;

    const loginResponse = await fetch(
      "http://localhost:8080/api/v1/user/login",
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, password }),
      }
    );

    if (!loginResponse.ok) {
      return NextResponse.json({ error: "Failed to login" }, { status: 401 });
    }

    const accessToken = (await loginResponse.json()).data["access"] || "";

    const response = NextResponse.json({ success: true });
    response.cookies.set({
      name: "accessToken",
      value: accessToken,
      httpOnly: true,
      path: "/",
    });

    return response;
  } catch (err) {
    console.error("Login route error:", err);
    return NextResponse.json({ error: "Server error" }, { status: 500 });
  }
}
