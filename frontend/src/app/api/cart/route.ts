import { NextRequest, NextResponse } from "next/server";
import { cookies } from "next/headers";
import client from "@/lib/backend/client";
import { components } from "@/lib/backend/generated/schema";

type CartResponse = {
  code?: string;
  msg?: string;
  data?: components["schemas"]["CartDto"];
};

export async function GET(req: NextRequest) {
  const token = (await cookies()).get("accessToken");
  if (!token) {
    return NextResponse.json({ error: "No token" }, { status: 401 });
  }

  const response = await client.GET("/api/v1/cart", {
    headers: {
      Authorization: `Bearer ${token.value}`,
    },
    credentials: "include",
  });

  if (response.error) {
    return NextResponse.json({ error: response["error"] }, { status: 400 });
  }

  const getCart = response.data as CartResponse;
  return NextResponse.json(getCart);
}

export async function PATCH(req: NextRequest) {
  const token = (await cookies()).get("accessToken");
  if (!token) {
    return NextResponse.json({ error: "No token" }, { status: 401 });
  }

  const body = await req.json();
  const { productId, quantity } = body as {
    productId: number;
    quantity: number;
  };

  const response = await client.PATCH("/api/v1/cart", {
    headers: { Authorization: `Bearer ${token.value}` },
    body: { productId, quantity },
    credentials: "include",
  });
  if (response.error) {
    return NextResponse.json({ error: response["error"] }, { status: 400 });
  }
  const updatedCart = response.data as CartResponse;
  return NextResponse.json(updatedCart);
}

export async function POST(req: NextRequest) {
  const token = (await cookies()).get("accessToken");
  if (!token) {
    return NextResponse.json({ error: "No token" }, { status: 401 });
  }

  const body = await req.json();
  const { productId, quantity } = body as {
    productId: number;
    quantity: number;
  };

  const response = await client.POST("/api/v1/cart", {
    headers: { Authorization: `Bearer ${token.value}` },
    body: { productId, quantity },
    credentials: "include",
  });
  if (response.error) {
    return NextResponse.json({ error: response["error"] }, { status: 400 });
  }
  const addedToCart = response.data as CartResponse;
  return NextResponse.json(addedToCart);
}

export async function POST_LOGOUT(req: NextRequest) {
  // 쿠키에서 accessToken 제거
  const cookieStore = await cookies();
  cookieStore.set("accessToken", "", { maxAge: -1, path: '/' }); // 쿠키 만료 설정

  return NextResponse.json({ success: true, message: "로그아웃 성공" });
}
