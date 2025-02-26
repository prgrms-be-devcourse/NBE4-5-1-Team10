import client from "@/lib/backend/client";
import { cookies } from "next/headers";
import { NextRequest, NextResponse } from "next/server";

export async function GET(req: NextRequest) {
  const token = (await cookies()).get("accessToken");
  if (!token) {
    return NextResponse.json({ error: "No token" }, { status: 401 });
  }

  const { searchParams } = new URL(req.url);
  const id = searchParams.get("id");

  if (!id) {
    return NextResponse.json({ error: "Missing id" }, { status: 400 });
  }

  const response = await client.GET("/api/v1/admin/orders/{id}", {
    params: {
      path: {
        id: Number(id),
      },
    },
    headers: { Authorization: `Bearer ${token.value}` },
    credentials: "include",
  });

  if (response.error) {
    return NextResponse.json({ error: response["error"] }, { status: 400 });
  }
  const order = response.data;
  return NextResponse.json(order);
}
