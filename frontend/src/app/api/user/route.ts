import client from "@/lib/backend/client";
import { cookies } from "next/headers";
import { NextRequest, NextResponse } from "next/server";

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
  const getUser = response.data;
  return NextResponse.json(getUser);
}
