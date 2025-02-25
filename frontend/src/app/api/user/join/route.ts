import { NextRequest, NextResponse } from "next/server";

export async function POST(req: NextRequest) {
  try {
    const body = await req.json();
    const { username, email, address, password } = body;

    const joinResponse = await fetch(
      "http://localhost:8080/api/v1/user/join",
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ username, email, address, password }),
      }
    );

    // 중복 이메일 처리
    if (joinResponse.status === 400) {
      const responseData = await joinResponse.json();
      if (responseData.code === "400-1") {
        return NextResponse.json({ error: "이미 사용중인 이메일입니다." }, { status: 400 });
      }
    }

    if (!joinResponse.ok) {
      return NextResponse.json({ error: "회원가입 실패" }, { status: 400 });
    }
    const responseData = await joinResponse.json();

    return NextResponse.json({
      success: true,
      data: responseData.data,
    });
  } catch (err) {
    console.error("Signup route error:", err);
    return NextResponse.json({ error: "Server error" }, { status: 500 });
  }
}