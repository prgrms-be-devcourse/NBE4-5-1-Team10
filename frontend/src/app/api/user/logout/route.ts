import { NextRequest, NextResponse } from "next/server";

export async function POST(req: NextRequest) {
  try {
    const authHeader = req.headers.get("Authorization");
    const token = authHeader?.split(" ")[1];

    const logoutResponse = await fetch(
      "http://localhost:8080/api/v1/user/logout",
      {
        method: "POST",
        headers: {
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      }
    );

    if (!logoutResponse.ok) {
      return NextResponse.json({ error: "Failed to log out" }, { status: 401 });
    }

    return NextResponse.json({ success: true });
  } catch (err) {
    console.error("Logout route error:", err);
    return NextResponse.json({ error: "Server error" }, { status: 500 });
  }
}