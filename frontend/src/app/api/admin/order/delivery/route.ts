import client from "@/lib/backend/client";
import { cookies } from "next/headers";
import { NextRequest, NextResponse } from "next/server";

export async function POST(req: NextRequest) {
  const token = (await cookies()).get("accessToken");
  if (!token) {
    return NextResponse.json({ error: "No token" }, { status: 401 });
  }

  const body = await req.json();
  const { id } = body as {
    id: number;
  };

  const response = await client.POST("/api/v1/admin/orders/{id}/delivery", {
    headers: { Authorization: `Bearer ${token.value}` },
    params: {
      path: {
        id,
      },
    },
    credentials: "include",
  });

  if (response.error) {
    return NextResponse.json({ error: response["error"] }, { status: 400 });
  }
  const delivery = response.data;
  return NextResponse.json(delivery);
}
