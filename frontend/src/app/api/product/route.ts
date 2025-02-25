import client from "@/lib/backend/client";
import { cookies } from "next/headers";
import { NextRequest, NextResponse } from "next/server";

export async function GET(req: NextRequest) {
  const body = await req.json();
  const { id } = body as {
    id: number;
  };

  const response = await client.GET("/api/v1/products/{productId}", {
    params: {
      path: {
        productId: id,
      },
    },
  });
  if (response.error) {
    return NextResponse.json({ error: response["error"] }, { status: 400 });
  }
  const product = response.data;
  return NextResponse.json(product);
}
