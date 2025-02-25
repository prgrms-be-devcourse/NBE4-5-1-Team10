import client from "@/lib/backend/client";
import { cookies } from "next/headers";
import { NextRequest, NextResponse } from "next/server";

export async function GET(req: NextRequest) {
  const response = await client.GET("/api/v1/products/count");
  if (response.error) {
    return NextResponse.json({ error: response["error"] }, { status: 400 });
  }
  const productCount = response.data;
  return NextResponse.json(productCount);
}
